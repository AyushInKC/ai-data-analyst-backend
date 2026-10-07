package com.ayush_singh.ai_data_analyst.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
public class DataRegistry {

    private final Map<String, String> datasets =
            new ConcurrentHashMap<>();

    private final DuckDBService duckDBService;

    private final Path uploadDirectory =
            Paths.get("data", "uploads");

    public DataRegistry(DuckDBService duckDBService) {
        this.duckDBService = duckDBService;
    }

    @PostConstruct
    public void restoreDatasets() {

        try {

            Files.createDirectories(uploadDirectory);

            try (Stream<Path> files =
                         Files.list(uploadDirectory)) {

                files
                        .filter(Files::isRegularFile)
                        .filter(path ->
                                path.getFileName()
                                        .toString()
                                        .toLowerCase()
                                        .endsWith(".csv")
                        )
                        .forEach(this::restoreDataset);
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to restore datasets",
                    e
            );
        }
    }

    private void restoreDataset(Path filePath) {

        String filename =
                filePath.getFileName().toString();

        String datasetId =
                filename.substring(
                        0,
                        filename.length() - 4
                );

        String tableName =
                "dataset_" + datasetId;

        duckDBService.loadCSV(
                tableName,
                filePath.toAbsolutePath().toString()
        );

        register(datasetId, tableName);

        System.out.println(
                "Restored dataset: "
                        + datasetId
                        + " -> "
                        + tableName
        );
    }

    public void register(
            String datasetId,
            String tableName
    ) {

        datasets.put(datasetId, tableName);
    }

    public String getTableName(String datasetId) {

        if (datasetId == null ||
                datasetId.isBlank()) {

            throw new IllegalArgumentException(
                    "Dataset ID is required"
            );
        }

        String tableName =
                datasets.get(datasetId);

        if (tableName == null) {

            throw new IllegalArgumentException(
                    "Dataset not found: " + datasetId
            );
        }

        return tableName;
    }
}