package com.bolingcavalry.simpleadvisor.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bolingcavalry.simpleadvisor.dto.SentimentAnalysis;
import com.bolingcavalry.simpleadvisor.dto.StoryInfo;

import lombok.Data;

@RestController
public class ChatController {
    // 日志
    private final Logger logger = LoggerFactory.getLogger(ChatController.class);

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
    @PostMapping("/structured/bean")
    public ResponseEntity<Response> doBean(@RequestBody PromptRequest request) {

        BeanOutputConverter<SentimentAnalysis> converter = new BeanOutputConverter<>(SentimentAnalysis.class);

        // 日志打印schema
        logger.info("schema: {}", converter.getJsonSchema());

        // 调用并直接获取对象
        SentimentAnalysis analysis = chatClient.prompt()
                .user("请分析以下文本的情感倾向和置信度：" + request.getPrompt())
                .call()
                .entity(converter);

        // 日志打印分析结果
        logger.info("analysis对象实例: {}", analysis);

        return ResponseEntity.ok(new Response(analysis.toString()));
    }

    @SuppressWarnings("null")
    @PostMapping("/structured/map")
    public ResponseEntity<Response> doMap(@RequestBody PromptRequest request) {

        MapOutputConverter converter = new MapOutputConverter();
        Map<String, Object> entity = chatClient.prompt()
                .user("从以下文本中提取姓名和职业信息：" + request.getPrompt())
                .call()
                .entity(converter);

        logger.info("entity: {}", entity);

        return ResponseEntity.ok(new Response(entity.toString()));
    }

    @SuppressWarnings("null")
    @PostMapping("/structured/list")
    public ResponseEntity<Response> doList(@RequestBody PromptRequest request) {

        BeanOutputConverter<List<StoryInfo>> converter = new BeanOutputConverter<>(
                new ParameterizedTypeReference<List<StoryInfo>>() {
                });

        List<StoryInfo> storyInfos = chatClient.prompt()
                .user(request.getPrompt())
                .call()
                .entity(converter);

        logger.info("storyInfos对象实例: {}", storyInfos);

        return ResponseEntity.ok(new Response(storyInfos.toString()));
    }
}
