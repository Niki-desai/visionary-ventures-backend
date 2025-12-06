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
public class VoyageService {

    private final RestTemplate restTemplate;
    private final RagConfig ragConfig;
    private static final String VOYAGE_API_URL = "https://api.voyageai.com/v1/embeddings";

    public VoyageService(RestTemplate restTemplate, RagConfig ragConfig) {
        this.restTemplate = restTemplate;
        this.ragConfig = ragConfig;
    }

    public List<Double> embedText(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ragConfig.getVoyageApiKey());

        Map<String, Object> body = new HashMap<>();
        body.put("input", List.of(text));
        body.put("model", "voyage-3-lite");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(VOYAGE_API_URL, request, Map.class);
            if (response.getBody() != null && response.getBody().containsKey("data")) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
                if (!data.isEmpty()) {
                    return (List<Double>) data.get(0).get("embedding");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate embedding: " + e.getMessage());
        }
        return List.of();
    }
}
