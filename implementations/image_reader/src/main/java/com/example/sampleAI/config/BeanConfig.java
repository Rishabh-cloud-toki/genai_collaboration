package com.example.sampleAI.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    @Bean
    public InMemoryChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
