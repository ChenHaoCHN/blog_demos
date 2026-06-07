package com.bolingcavalry.simpleadvisor.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.Data;

@RestController
public class ChatController {

    @Autowired
    private DeepSeekChatModel chatModel;


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

    @PostMapping("/deepseek/chat")
    public ResponseEntity<Response> chat(@RequestBody PromptRequest request) {
        String response = chatModel.call(request.getPrompt());
        return ResponseEntity.ok(new Response(response));
    }
}
