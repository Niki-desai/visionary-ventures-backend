package com.jobbot.service.rag;

import com.jobbot.config.RagConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroqService {

    private final RestTemplate restTemplate;
    private final RagConfig ragConfig;
    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";

    public GroqService(RestTemplate restTemplate, RagConfig ragConfig) {
        this.restTemplate = restTemplate;
        this.ragConfig = ragConfig;
    }

    @SuppressWarnings("unchecked")
    public String askGroq(String prompt, String context) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ragConfig.getGroqApiKey());

        String systemPrompt = "You are a helpful assistant for analyzing resumes and job descriptions. " +
                "Use the following context to answer the user's question. " +
                "If the answer is not in the context, say so.\n\nContext:\n" + context;

        Map<String, Object> messageSystem = new HashMap<>();
        messageSystem.put("role", "system");
        messageSystem.put("content", systemPrompt);

        Map<String, Object> messageUser = new HashMap<>();
        messageUser.put("role", "user");
        messageUser.put("content", prompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.1-8b-instant");
        body.put("messages", List.of(messageSystem, messageUser));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(GROQ_API_URL, request, Map.class);
            if (response.getBody() != null && response.getBody().containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to call Groq API: " + e.getMessage());
        }
        return "Error: Could not get response from Groq.";
    }
}
