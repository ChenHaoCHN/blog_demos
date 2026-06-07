package com.bolingcavalry.helloworld.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.Data;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Data
    static class PromptRequest {
        private String prompt;
    }

    @Data
    static class Response {
        private String result;

        public Response(String result) {
            this.result = result;
        }
    }

    @PostMapping("/chat")
    public ResponseEntity<Response> chat(@RequestBody PromptRequest request) {
        try {
            String content = chatClient.prompt()
                    .user(request.getPrompt())
                    .call()
                    .content();
            
            return ResponseEntity.ok(new Response(content));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new Response("Error: " + e.getMessage()));
        }
    }
}
