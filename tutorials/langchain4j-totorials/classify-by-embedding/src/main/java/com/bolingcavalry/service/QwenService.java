package com.bolingcavalry.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolingcavalry.vo.CustomerServiceCategory;

import dev.langchain4j.classification.TextClassifier;

/**
 * 通义千问服务类，用于与通义千问模型进行交互
 */
@Service
public class QwenService {

    private static final Logger logger = LoggerFactory.getLogger(QwenService.class);

    @Autowired
    private TextClassifier<CustomerServiceCategory> classifier;

    /**
     * 通过向量搜索实现分类
     * 
     * @param prompt 用户消息
     * @return 助手生成的回答
     */
    public String classify(String prompt) {
        List<CustomerServiceCategory> categories = classifier.classify(prompt);
        logger.info("响应：" + categories);
        return categories + " [from classify]";
    }


}
