package com.bolingcavalry.simpleadvisor.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class LoggerAdvisor implements CallAdvisor {

    private static final Logger log = LoggerFactory.getLogger(LoggerAdvisor.class);

    @SuppressWarnings("null")
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        String model = request.prompt().getOptions() != null
                ? request.prompt().getOptions().getModel()
                : "unknown";
        log.info("【AI 请求】模型: {}, Prompt: {}", model, request.prompt().getContents());

        ChatClientResponse response = chain.nextCall(request);

        ChatResponse chatResponse = response.chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        if (content == null) {
            content = "";
        }
        log.info("【AI 响应】Token 消耗: {}, 内容: {}",
                chatResponse.getMetadata().getUsage().getTotalTokens(),
                content);

        return response;
    }

    @SuppressWarnings("null")
    @Override
    public String getName() {
        return "logger-advisor";
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE+1;
    }
}
