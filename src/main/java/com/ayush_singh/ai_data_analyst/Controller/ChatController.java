package com.ayush_singh.ai_data_analyst.Controller;

import com.ayush_singh.ai_data_analyst.Model.ChatRequest;
import com.ayush_singh.ai_data_analyst.Model.ChatResponse;
import com.ayush_singh.ai_data_analyst.Service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @RequestBody ChatRequest request
    ) {

        return ResponseEntity.ok(
                chatService.chat(request)
        );
    }

}
