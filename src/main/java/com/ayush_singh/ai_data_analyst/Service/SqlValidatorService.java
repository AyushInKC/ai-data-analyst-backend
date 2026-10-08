package com.ayush_singh.ai_data_analyst.Service;

import org.springframework.stereotype.Service;

import java.util.Locale;
@Service
public class SqlValidatorService {
    public String validate(String sql, String expectedTable) {

        if (sql == null || sql.isBlank()) {
            throw new IllegalArgumentException(
                    "AI generated an empty SQL query"
            );
        }


        sql = cleanSql(sql);

        String normalized = sql
                .trim()
                .toLowerCase(Locale.ROOT);


        if (!normalized.startsWith("select ")
                && !normalized.startsWith("select\n")
                && !normalized.startsWith("with ")) {

            throw new IllegalArgumentException(
                    "Only SELECT and WITH queries are allowed"
            );
        }


        String[] forbidden = {
                "insert ",
                "update ",
                "delete ",
                "drop ",
                "alter ",
                "create ",
                "truncate ",
                "replace ",
                "merge ",
                "grant ",
                "revoke "
        };

        for (String keyword : forbidden) {
            if (normalized.contains(keyword)) {
                throw new IllegalArgumentException(
                        "Unsafe SQL detected: " + keyword.trim()
                );
            }
        }


        String expected = expectedTable.toLowerCase(Locale.ROOT);

        if (!normalized.contains(expected)) {
            throw new IllegalArgumentException(
                    "Generated SQL does not reference the requested dataset"
            );
        }

        return sql;
    }

    private String cleanSql(String sql) {

        sql = sql.trim();

        if (sql.startsWith("```sql")) {
            sql = sql.substring(6);
        } else if (sql.startsWith("```")) {
            sql = sql.substring(3);
        }

        if (sql.endsWith("```")) {
            sql = sql.substring(0, sql.length() - 3);
        }

        return sql.trim();
    }
}
