package com.bolingcavalry.simpleadvisor.advisor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.Ordered;

public class RAGContextAdvisor implements CallAdvisor {

    private final VectorStore vectorStore;

    public RAGContextAdvisor(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public String getName() {
        return "RAGContextAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        Prompt originalPrompt = request.prompt();

        // 拿到原始提示词
        String query = originalPrompt.getInstructions().stream()
                .filter(msg -> msg instanceof UserMessage)
                .map(msg -> (UserMessage) msg)
                .map(UserMessage::getText)
                .findFirst()
                .orElse("");

        if (query.isEmpty()) {
            return chain.nextCall(request);
        }

        // 从向量存储中获取相关文档
        List<Document> relevantDocs = vectorStore.similaritySearch(
                SearchRequest.builder().query(query).topK(3).build());

        // 把搜索到的文档内容拼接起来
        String context = relevantDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n---\n"));

        // 准备最终给到模型的系统提示词，注意是系统提示词
        String enhancedSystemText = String.format(
                "基于以下参考内容回答：\n%s\n\n如果无法回答，请说明。",
                context);

        List<Message> newMessages = new ArrayList<>();
        newMessages.add(new SystemMessage(enhancedSystemText));

        originalPrompt.getInstructions().forEach(msg -> {
            if (!(msg instanceof SystemMessage)) {
                newMessages.add(msg);
            }
        });

        Prompt newPrompt = new Prompt(newMessages);
        ChatClientRequest modifiedRequest = new ChatClientRequest(
                newPrompt,
                request.context());

        return chain.nextCall(modifiedRequest);
    }
}
