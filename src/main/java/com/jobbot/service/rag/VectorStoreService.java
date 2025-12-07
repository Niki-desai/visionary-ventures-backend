package com.jobbot.service.rag;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VectorStoreService {

    private static final Logger logger = LoggerFactory.getLogger(VectorStoreService.class);
    private static final String VECTOR_STORE_FILE = "vectorstore.json";
    private static final int CHUNK_SIZE = 300; // words

    private final FileService fileService;
    private final VoyageService voyageService;
    private final ObjectMapper objectMapper;

    private List<VectorDocument> vectorStore = new ArrayList<>();

    public VectorStoreService(FileService fileService, VoyageService voyageService, ObjectMapper objectMapper) {
        this.fileService = fileService;
        this.voyageService = voyageService;
        this.objectMapper = objectMapper;
        loadVectorStore();
    }

    public void buildVectorStore() {
        logger.info("Building vector store...");
        List<FileService.FileDocument> documents = fileService.loadDocuments();
        List<VectorDocument> newVectorStore = new ArrayList<>();

        for (FileService.FileDocument doc : documents) {
            List<String> chunks = chunkText(doc.getContent());
            for (String chunk : chunks) {
                List<Double> embedding = voyageService.embedText(chunk);
                if (!embedding.isEmpty()) {
                    newVectorStore.add(new VectorDocument(doc.getFilename(), chunk, embedding));
                }
            }
        }

        vectorStore = newVectorStore;
        saveVectorStore();
        logger.info("Vector store built with {} chunks.", vectorStore.size());
    }

    public List<String> search(String query, int topK) {
        List<Double> queryEmbedding = voyageService.embedText(query);
        if (queryEmbedding.isEmpty())
            return List.of();

        return vectorStore.stream()
                .map(doc -> new ScoredDocument(doc, cosineSimilarity(queryEmbedding, doc.getEmbedding())))
                .sorted(Comparator.comparingDouble(ScoredDocument::getScore).reversed())
                .limit(topK)
                .map(sd -> sd.getDocument().getContent())
                .collect(Collectors.toList());
    }

    private List<String> chunkText(String text) {
        String[] words = text.split("\\s+");
        List<String> chunks = new ArrayList<>();
        StringBuilder currentChunk = new StringBuilder();
        int wordCount = 0;

        for (String word : words) {
            currentChunk.append(word).append(" ");
            wordCount++;
            if (wordCount >= CHUNK_SIZE) {
                chunks.add(currentChunk.toString().trim());
                currentChunk = new StringBuilder();
                wordCount = 0;
            }
        }
        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString().trim());
        }
        return chunks;
    }

    private double cosineSimilarity(List<Double> vecA, List<Double> vecB) {
        if (vecA.size() != vecB.size())
            return 0.0;
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vecA.size(); i++) {
            dotProduct += vecA.get(i) * vecB.get(i);
            normA += Math.pow(vecA.get(i), 2);
            normB += Math.pow(vecB.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private void saveVectorStore() {
        try {
            objectMapper.writeValue(new File(VECTOR_STORE_FILE), vectorStore);
        } catch (IOException e) {
            logger.error("Failed to save vector store", e);
        }
    }

    private void loadVectorStore() {
        File file = new File(VECTOR_STORE_FILE);
        if (file.exists()) {
            try {
                vectorStore = objectMapper.readValue(file, new TypeReference<List<VectorDocument>>() {
                });
                logger.info("Loaded vector store with {} chunks.", vectorStore.size());
            } catch (IOException e) {
                logger.error("Failed to load vector store", e);
            }
        }
    }

    // Inner classes for data structures
    public static class VectorDocument {
        private String filename;
        private String content;
        private List<Double> embedding;

        public VectorDocument() {
        } // For Jackson

        public VectorDocument(String filename, String content, List<Double> embedding) {
            this.filename = filename;
            this.content = content;
            this.embedding = embedding;
        }

        public String getFilename() {
            return filename;
        }

        public String getContent() {
            return content;
        }

        public List<Double> getEmbedding() {
            return embedding;
        }
    }

    private static class ScoredDocument {
        private final VectorDocument document;
        private final double score;

        public ScoredDocument(VectorDocument document, double score) {
            this.document = document;
            this.score = score;
        }

        public VectorDocument getDocument() {
            return document;
        }

        public double getScore() {
            return score;
        }
    }
}
