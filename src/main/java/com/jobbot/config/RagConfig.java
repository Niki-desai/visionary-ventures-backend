package com.jobbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;

@Configuration
public class RagConfig {

    @Value("${rag.groq.api-key}")
    private String groqApiKey;

    @Value("${rag.voyage.api-key}")
    private String voyageApiKey;

    public String getGroqApiKey() {
        return groqApiKey;
    }

    public String getVoyageApiKey() {
        return voyageApiKey;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
