package com.ayush_singh.ai_data_analyst.Model;

import java.util.List;

public record DataSetResponse(
        String datasetId,
        String fileName,
        long rowCount,
        int columnCount,
        List<String> columns
) {
}
