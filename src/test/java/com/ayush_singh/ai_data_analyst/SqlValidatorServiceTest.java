package com.ayush_singh.ai_data_analyst;

import com.ayush_singh.ai_data_analyst.Service.SqlValidatorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class SqlValidatorServiceTest {

    private final SqlValidatorService validator =
            new SqlValidatorService();

    private final String table =
            "dataset_90c440247f8a4b05953afd8cfaa4f3c6";


    @Test
    public void shouldAllowSelectQuery() {

        String sql = """
            SELECT SUM(revenue)
            FROM dataset_90c440247f8a4b05953afd8cfaa4f3c6;
            """;

        String result = validator.validate(sql, table);

        assertEquals(sql.trim(), result);
    }

    @Test
    public void shouldAllowWithQuery() {

        String sql = """
                WITH data AS (
                    SELECT revenue
                    FROM dataset_90c440247f8a4b05953afd8cfaa4f3c6
                )
                SELECT SUM(revenue)
                FROM data;
                """;

        assertDoesNotThrow(() ->
                validator.validate(sql, table)
        );
    }

    @Test
    public void shouldRejectEmptySql() {

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate("", table)
        );
    }

    @Test
    public void shouldRejectNullSql() {

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(null, table)
        );
    }

    @Test
    public void shouldRejectInsert() {

        String sql = """
                INSERT INTO dataset_90c440247f8a4b05953afd8cfaa4f3c6
                VALUES (1);
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(sql, table)
        );
    }

    @Test
    public void shouldRejectUpdate() {

        String sql = """
                UPDATE dataset_90c440247f8a4b05953afd8cfaa4f3c6
                SET revenue = 0;
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(sql, table)
        );
    }

    @Test
    public void shouldRejectDelete() {

        String sql = """
                DELETE FROM dataset_90c440247f8a4b05953afd8cfaa4f3c6;
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(sql, table)
        );
    }

    @Test
    public void shouldRejectDrop() {

        String sql = """
                DROP TABLE dataset_90c440247f8a4b05953afd8cfaa4f3c6;
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(sql, table)
        );
    }

    @Test
    public void shouldRejectAlter() {

        String sql = """
                ALTER TABLE dataset_90c440247f8a4b05953afd8cfaa4f3c6
                ADD COLUMN test VARCHAR;
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(sql, table)
        );
    }

    @Test
    public void shouldRejectCreate() {

        String sql = """
                CREATE TABLE malicious_table (
                    id INTEGER
                );
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(sql, table)
        );
    }

    @Test
    public void shouldRejectTruncate() {

        String sql = """
                TRUNCATE TABLE dataset_90c440247f8a4b05953afd8cfaa4f3c6;
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(sql, table)
        );
    }

    @Test
    public void shouldRejectWrongTable() {

        String sql = """
                SELECT SUM(revenue)
                FROM another_table;
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(sql, table)
        );
    }

    @Test
    public void shouldRemoveSqlMarkdownCodeFence() {

        String sql = """
                ```sql
                SELECT SUM(revenue)
                FROM dataset_90c440247f8a4b05953afd8cfaa4f3c6;
                ```
                """;

        String result = validator.validate(sql, table);

        assertTrue(result.startsWith("SELECT"));
        assertFalse(result.contains("```"));
    }

    @Test
    public void shouldAllowAggregateQuery() {

        String sql = """
                SELECT
                    SUM(revenue) AS total_revenue,
                    AVG(revenue) AS average_revenue,
                    SUM(quantity) AS total_quantity
                FROM dataset_90c440247f8a4b05953afd8cfaa4f3c6;
                """;

        assertDoesNotThrow(() ->
                validator.validate(sql, table)
        );
    }

    @Test
    public void shouldAllowGroupByQuery() {

        String sql = """
                SELECT region, SUM(revenue) AS total_revenue
                FROM dataset_90c440247f8a4b05953afd8cfaa4f3c6
                GROUP BY region;
                """;

        assertDoesNotThrow(() ->
                validator.validate(sql, table)
        );
    }
}