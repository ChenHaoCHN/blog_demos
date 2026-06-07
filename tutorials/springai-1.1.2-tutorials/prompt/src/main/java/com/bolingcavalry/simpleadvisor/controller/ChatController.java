package com.bolingcavalry.simpleadvisor.controller;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@RestController
public class ChatController {

    private final static String LANGUAGE = "python";
    private final static int MAX_WORDS = 200;

    @Autowired
    private ChatClient chatClient;

    // 使用 @Value 注入 classpath 下的模板文件
    @Value("classpath:/prompts/system-message-01.st")
    private Resource systemResource;

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
    @PostMapping("/withresource")
    public ResponseEntity<Response> chat(@RequestBody PromptRequest request) {

        // 使用 Resource 创建 SystemPromptTemplate
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemResource);

        // 填充模板变量，生成 SystemMessage
        Message systemMessage = systemPromptTemplate.createMessage(Map.of(
                "language", LANGUAGE,
                "maxWords", MAX_WORDS));

        // 创建 UserMessage
        Message userMessage = new UserMessage(request.getPrompt());

        // 组合成 Prompt
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        try {
            String content = chatClient.prompt(prompt)
                    .call()
                    .content();

            return ResponseEntity.ok(new Response(content));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new Response("Error: " + e.getMessage()));
        }
    }
}
