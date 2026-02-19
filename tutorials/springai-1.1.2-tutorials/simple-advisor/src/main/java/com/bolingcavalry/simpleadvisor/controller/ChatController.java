package com.bolingcavalry.simpleadvisor.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.Data;

@RestController
public class ChatController {

    @Autowired
    private ChatClient chatClient;


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

    @SuppressWarnings("null")
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
