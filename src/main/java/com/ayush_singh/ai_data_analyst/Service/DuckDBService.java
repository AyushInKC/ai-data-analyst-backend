package com.ayush_singh.ai_data_analyst.Service;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DuckDBService {

    private final Connection connection;

    public DuckDBService() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:duckdb:data/analytics.duckdb"
            );
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to initialize DuckDB",
                    e
            );
        }
    }

    public void loadCSV(String tableName, String csvPath) {

        String sql = """
                CREATE OR REPLACE TABLE %s AS
                SELECT *
                FROM read_csv_auto('%s')
                """.formatted(tableName, csvPath);

        try (Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to load CSV into DuckDB",
                    e
            );
        }
    }

    public boolean tableExists(String tableName) {

        String sql = """
                SELECT COUNT(*)
                FROM information_schema.tables
                WHERE table_name = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, tableName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                resultSet.next();

                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to check table",
                    e
            );
        }
    }

    public long getRowCount(String tableName) {

        String sql = "SELECT COUNT(*) FROM " + tableName;

        try (Statement statement =
                     connection.createStatement();
             ResultSet resultSet =
                     statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getLong(1);
            }

            return 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to count rows",
                    e
            );
        }
    }

    public List<String> getColumns(String tableName) {

        String sql = "DESCRIBE " + tableName;

        List<String> columns = new ArrayList<>();

        try (Statement statement =
                     connection.createStatement();
             ResultSet resultSet =
                     statement.executeQuery(sql)) {

            while (resultSet.next()) {

                columns.add(
                        resultSet.getString("column_name")
                );
            }

            return columns;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve columns",
                    e
            );
        }
    }

    public List<Map<String, Object>> executeQuery(String sql) {

        List<Map<String, Object>> results =
                new ArrayList<>();

        try (Statement statement =
                     connection.createStatement();
             ResultSet resultSet =
                     statement.executeQuery(sql)) {

            ResultSetMetaData metadata =
                    resultSet.getMetaData();

            int columnCount =
                    metadata.getColumnCount();

            while (resultSet.next()) {

                Map<String, Object> row =
                        new java.util.LinkedHashMap<>();

                for (int i = 1; i <= columnCount; i++) {

                    String columnName =
                            metadata.getColumnName(i);

                    row.put(
                            columnName,
                            resultSet.getObject(i)
                    );
                }

                results.add(row);
            }

            return results;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to execute SQL",
                    e
            );
        }
    }
}