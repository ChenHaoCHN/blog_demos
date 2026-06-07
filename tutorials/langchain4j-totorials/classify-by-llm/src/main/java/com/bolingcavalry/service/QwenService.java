package com.bolingcavalry.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolingcavalry.vo.Sentiment;

/**
 * 通义千问服务类，用于与通义千问模型进行交互
 */
@Service
public class QwenService {

    private static final Logger logger = LoggerFactory.getLogger(QwenService.class);

    @Autowired
    private Assistant assistant;

    /**
     * 把用户信息发给LLM，让大模型判断情感
     * 
     * @param prompt 用户消息
     * @return 助手生成的回答
     */
    public String analyzeSentimentOf(String prompt) {
        Sentiment sentiment = assistant.analyzeSentimentOf(prompt);
        logger.info("响应：" + sentiment);
        return sentiment + " [from analyzeSentimentOf]";
    }

    /**
     * 判断情感是否为正面
     * 
     * @param prompt
     * @return 是否为正面情感
     */
    public String isPositive(String prompt) {
        boolean isPositive = assistant.isPositive(prompt);
        logger.info("响应：" + isPositive);
        return isPositive + " [from isPositive]";
    }
}
