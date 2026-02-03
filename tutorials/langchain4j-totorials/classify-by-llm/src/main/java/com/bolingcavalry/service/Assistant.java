package com.bolingcavalry.service;

import com.bolingcavalry.vo.Sentiment;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Assistant {
    /**
     * 把用户信息发给LLM，让大模型判断情感
     * 
     * @param text 用户消息
     * @return 助手生成的回答
     */
    @UserMessage("请根据用户的消息判断情感，用户消息是：{{it}}")
    Sentiment analyzeSentimentOf(String text);

    /**
     * 判断情感是否为正面
     * 
     * @param text 用户消息
     * @return 是否为正面情感
     */
    @UserMessage("请判断用户的消息情感是否为正面，用户消息是：{{it}}")
    boolean isPositive(String text);
}