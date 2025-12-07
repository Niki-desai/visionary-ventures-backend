package com.jobbot.service.rag;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    private static final String DOCS_DIR = "docs";

    public List<FileDocument> loadDocuments() {
        List<FileDocument> documents = new ArrayList<>();
        File folder = new File(DOCS_DIR);

        if (!folder.exists() || !folder.isDirectory()) {
            logger.warn("Docs directory not found: {}", DOCS_DIR);
            return documents;
        }

        File[] files = folder.listFiles();
        if (files == null)
            return documents;

        for (File file : files) {
            if (file.isFile() && isSupported(file.getName())) {
                try {
                    String content = Files.readString(file.toPath());
                    documents.add(new FileDocument(file.getName(), content));
                    logger.info("Loaded file: {}", file.getName());
                } catch (IOException e) {
                    logger.error("Failed to read file: {}", file.getName(), e);
                }
            }
        }
        return documents;
    }

    private boolean isSupported(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".txt") || lower.endsWith(".md") || lower.endsWith(".json");
    }

    public static class FileDocument {
        private String filename;
        private String content;

        public FileDocument(String filename, String content) {
            this.filename = filename;
            this.content = content;
        }

        public String getFilename() {
            return filename;
        }

        public String getContent() {
            return content;
        }
    }
}
