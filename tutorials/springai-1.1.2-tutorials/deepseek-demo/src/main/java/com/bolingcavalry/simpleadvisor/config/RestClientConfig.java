package com.bolingcavalry.simpleadvisor.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import com.bolingcavalry.simpleadvisor.advisor.LoggerAdvisor;

import java.util.List;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .messageConverters(converters -> {
                    // 查找并修改现有的MappingJackson2HttpMessageConverter
                    for (var converter : converters) {
                        if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) {
                            // 添加对application/octet-stream的支持
                            jacksonConverter.setSupportedMediaTypes(List.of(
                                    MediaType.APPLICATION_JSON,
                                    MediaType.APPLICATION_OCTET_STREAM
                            ));
                        }
                    }
                });
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, LoggerAdvisor loggerAdvisor) {
        return builder
                .defaultAdvisors(loggerAdvisor)  // 关键：在这里指定
                .build();
    }
}
