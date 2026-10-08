package com.ayush_singh.ai_data_analyst.Service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConversationService {

    private final ConcurrentHashMap<String, List<String>> conversations =
            new ConcurrentHashMap<>();

    public void addMessage(String sessionId, String message) {

        conversations
                .computeIfAbsent(
                        sessionId,
                        key -> new ArrayList<>()
                )
                .add(message);
    }

    public String getContext(String sessionId) {

        List<String> messages =
                conversations.get(sessionId);

        if (messages == null || messages.isEmpty()) {
            return "";
        }

        return String.join("\n", messages);
    }

    public void clearSession(String sessionId) {
        conversations.remove(sessionId);
    }
}