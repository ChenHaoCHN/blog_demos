package com.bolingcavalry.simpleadvisor.advisor;

import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component
public class TokenGuardAdvisor implements CallAdvisor {
    
    private static final Logger log = LoggerFactory.getLogger(TokenGuardAdvisor.class);

    // 最大允许的 Token 数量，按照您的模型自行调整
    private static final int MAX_TOKENS = 100;
    
    @SuppressWarnings("null")
    @Override
    public String getName() {
        return "tokenguard-advisor";
    }
    
    @Override
    public int getOrder() {
        // 最高优先级，确保最先执行（尽早拦截）
        return Ordered.HIGHEST_PRECEDENCE+2; 
    }

    @SuppressWarnings("null")
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        // 1. 获取原始 Prompt
        Prompt originalPrompt = request.prompt();
        
        // 2. 查找并处理 UserMessage
        List<Message> modifiedMessages = new ArrayList<>();
        boolean modified = false;
        
        for (Message message : originalPrompt.getInstructions()) {
            if (message instanceof UserMessage userMsg) {
                String content = userMsg.getText();
                
                // 简单估算 Token（中文按 0.8 字符/Token）
                int estimatedTokens = (int) (content.length() * 0.8);
                
                if (estimatedTokens > MAX_TOKENS) {
                    log.warn("输入过长: {} tokens，自动截断至 {}", estimatedTokens, MAX_TOKENS);
                    
                    // 截断文本
                    int truncatedLength = (int) (MAX_TOKENS / 0.8);
                    String truncatedContent = content.substring(0, truncatedLength);
                    

                    UserMessage truncatedMessage = UserMessage.builder()
                            .text(truncatedContent)
                            .media(userMsg.getMedia())
                            .metadata(userMsg.getMetadata())
                            .build();

                    // 创建新的 UserMessage（保留原消息的元数据）
                    modifiedMessages.add(truncatedMessage);
                    modified = true;
                } else {
                    modifiedMessages.add(message);
                }
            } else {
                modifiedMessages.add(message);
            }
        }
        
        // 3. 重建 ChatClientRequest（如果发生了修改）
        ChatClientRequest modifiedRequest = request;
        if (modified) {
            Prompt newPrompt = new Prompt(modifiedMessages);
            // 通过构造函数重建（1.1.2 中 ChatClientRequest 是标准数据类）
            modifiedRequest = new ChatClientRequest(newPrompt, request.context());
        }
        
        // 4. 继续调用链（必须调用，否则请求不会到达模型）
        return chain.nextCall(modifiedRequest);
    }
}