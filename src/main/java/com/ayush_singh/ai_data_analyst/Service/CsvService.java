package com.ayush_singh.ai_data_analyst.Service;

import com.ayush_singh.ai_data_analyst.Model.DataSetResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class CsvService {
    private final DuckDBService duckDBService;
    private final DataRegistry datasetRegistry;
    private final Path uploadDirectory = Paths.get("data", "uploads");

    public CsvService(DuckDBService duckDBService,DataRegistry datasetRegistry) {
        this.duckDBService = duckDBService;
        this.datasetRegistry=datasetRegistry;
    }

    public DataSetResponse upload(MultipartFile file) {

        validate(file);

        try {
            Files.createDirectories(uploadDirectory);

            String datasetId = UUID.randomUUID()
                    .toString()
                    .replace("-", "");

            String originalFilename = file.getOriginalFilename();

            String extension = ".csv";

            String filename = datasetId + extension;

            Path filePath = uploadDirectory.resolve(filename);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            String tableName = "dataset_" + datasetId;

            duckDBService.loadCSV(
                    tableName,
                    filePath.toAbsolutePath().toString()
            );

            datasetRegistry.register(
                    datasetId,
                    tableName
            );

            long rowCount =
                    duckDBService.getRowCount(tableName);

            List<String> columns =
                    duckDBService.getColumns(tableName);

            return new DataSetResponse(
                    datasetId,
                    originalFilename,
                    rowCount,
                    columns.size(),
                    columns
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to save uploaded file", e
            );
        }
    }

    private void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "CSV file cannot be empty"
            );
        }

        String filename = file.getOriginalFilename();

        if (filename == null ||
                !filename.toLowerCase().endsWith(".csv")) {

            throw new IllegalArgumentException(
                    "Only CSV files are supported"
            );
        }
    }
}
