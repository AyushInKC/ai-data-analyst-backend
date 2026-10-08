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
    private final SqlValidatorService sqlValidatorService;
    private final ConversationService conversationService;

    public ChatService(
            DataRegistry datasetRegistry,
            DuckDBService duckDBService,
            AiService aiService,
            SqlValidatorService sqlValidatorService,
            ConversationService conversationService

    ) {
        this.datasetRegistry = datasetRegistry;
        this.duckDbService = duckDBService;
        this.aiService = aiService;
        this.sqlValidatorService = sqlValidatorService;
        this.conversationService = conversationService;
    }

    public ChatResponse chat(ChatRequest request) {

        System.out.println("1. Dataset lookup started");

        String tableName =
                datasetRegistry.getTableName(request.datasetId());

        System.out.println("2. Dataset found: " + tableName);
        System.out.println("3. Generating SQL...");

        List<String> columns =
                duckDbService.getColumns(tableName);

        System.out.println("Columns: " + columns);

        String context =
                conversationService.getContext(request.sessionId());

        String generatedSql =
                aiService.generateSql(
                        tableName,
                        columns,
                        request.question(),
                        context
                );

        String sql =
                sqlValidatorService.validate(
                        generatedSql,
                        tableName
                );

        System.out.println("4. SQL generated: " + sql);
        System.out.println("5. Executing SQL...");

        List<Map<String, Object>> result =
                duckDbService.executeQuery(sql);

        System.out.println(
                "6. SQL executed. Rows: " + result.size()
        );

        System.out.println("7. Generating final answer...");

        String answer =
                aiService.generateAnswer(
                        request.question(),
                        sql,
                        result
                );

        System.out.println("8. Final answer generated");

        conversationService.addMessage(
                request.sessionId(),
                "User: " + request.question()
        );

        conversationService.addMessage(
                request.sessionId(),
                "SQL: " + sql
        );

        conversationService.addMessage(
                request.sessionId(),
                "Assistant: " + answer
        );

        return new ChatResponse(
                answer,
                sql
        );
    }

}
