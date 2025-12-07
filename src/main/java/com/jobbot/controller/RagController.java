package com.jobbot.controller;

import com.jobbot.service.rag.GroqService;
import com.jobbot.service.rag.VectorStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final VectorStoreService vectorStoreService;
    private final GroqService groqService;

    public RagController(VectorStoreService vectorStoreService, GroqService groqService) {
        this.vectorStoreService = vectorStoreService;
        this.groqService = groqService;
    }

    @PostMapping("/build")
    public ResponseEntity<String> buildVectorStore() {
        vectorStoreService.buildVectorStore();
        return ResponseEntity.ok("Vector store built successfully.");
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Query is required"));
        }

        // 1. Search for relevant context
        List<String> contextChunks = vectorStoreService.search(query, 3);
        String context = String.join("\n\n", contextChunks);

        // 2. Ask Groq with context
        String answer = groqService.askGroq(query, context);

        return ResponseEntity.ok(Map.of(
                "query", query,
                "answer", answer,
                "context_used", context));
    }
}
