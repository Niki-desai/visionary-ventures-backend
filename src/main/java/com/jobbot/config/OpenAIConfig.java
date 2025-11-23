package com.jobbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Value("${ai.openai.api-key}")
    private String apiKey;

    @Value("${ai.openai.model}")
    private String model;

    @Value("${ai.openai.temperature}")
    private Double temperature;

    @Value("${ai.openai.max-tokens}")
    private Integer maxTokens;

    public String getApiKey() {
        return apiKey;
    }

    public String getModel() {
        return model;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }
}

