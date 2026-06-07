package com.bolingcavalry.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bolingcavalry.vo.IndexConfig;

@Configuration
public class LangChain4jConfig {

    @Value("${rag.file.path}")
    private String ragFilePath;

    @Value("${clickhouse.url}")
    private String clickhouseUrl;

    @Value("${clickhouse.table}")
    private String clickhouseTable;

    @Value("${clickhouse.username}")
    private String clickhouseUsername;

    @Value("${clickhouse.pswd}")
    private String clickhousePswd;

    @Bean
    public IndexConfig clickHouseConfig() {
        IndexConfig config = new IndexConfig();
        config.setCkURL(clickhouseUrl);
        config.setCkTableName(clickhouseTable);
        config.setCkUsername(clickhouseUsername);
        config.setCkPassword(clickhousePswd);
        config.setRagFilePath(ragFilePath);
        return config;
    }
}