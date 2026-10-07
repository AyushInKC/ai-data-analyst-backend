package com.ayush_singh.ai_data_analyst.Service;

import com.ayush_singh.ai_data_analyst.Model.ChatRequest;
import com.ayush_singh.ai_data_analyst.Model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class ChatService {

    private final DataRegistry datasetRegistry;
    private final DuckDBService duckDbService;
    private final AiService aiService;

    public ChatService(
            DataRegistry datasetRegistry,
            DuckDBService duckDbService,
            AiService aiService
    ) {
        this.datasetRegistry = datasetRegistry;
        this.duckDbService = duckDbService;
        this.aiService = aiService;
    }

    public ChatResponse chat(ChatRequest request) {
        System.out.println("1. Dataset lookup started");

        String tableName =
                datasetRegistry.getTableName(
                        request.datasetId()
                );
        System.out.println("2. Dataset found: " + tableName);
        System.out.println("3. Generating SQL...");

        String sql =
                aiService.generateSql(
                        tableName,
                        request.question()
                );
        System.out.println("4. SQL generated: " + sql);
        System.out.println("5. Executing SQL...");

        List<Map<String, Object>> result =
                duckDbService.executeQuery(sql);
        System.out.println("6. SQL executed. Rows: " + result.size());
        System.out.println("7. Generating final answer...");

        String answer =
                aiService.generateAnswer(
                        request.question(),
                        sql,
                        result
                );
        System.out.println("8. Final answer generated");

        return new ChatResponse(
                answer,
                sql
        );
    }
}
