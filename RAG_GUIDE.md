# RAG (Retrieval Augmented Generation) - Complete Guide

## 🎯 What is RAG?

**RAG = Retrieval Augmented Generation**

RAG is a technique that combines:
1. **Retrieval** - Finding relevant information from a knowledge base
2. **Augmented** - Enhancing the LLM prompt with retrieved context
3. **Generation** - LLM generates response using both its training data + retrieved context

**Simple analogy:**
- **Without RAG:** LLM answers from memory (training data) - may be outdated or incomplete
- **With RAG:** LLM first searches your documents, then answers using both memory + your documents

---

## 🤔 Why Use RAG?

### Problems RAG Solves:

1. **Outdated Information**
   - LLMs are trained on data up to a certain date
   - RAG allows access to latest information from your documents

2. **Domain-Specific Knowledge**
   - LLMs don't know your company's internal docs, policies, or specific data
   - RAG injects your knowledge into responses

3. **Hallucinations**
   - LLMs sometimes make up facts
   - RAG grounds responses in actual documents (reduces hallucinations)

4. **Cost & Privacy**
   - Don't need to fine-tune models (expensive)
   - Keep sensitive data in your database, not in model training

5. **Transparency**
   - Can show which documents were used (citations)
   - Users can verify sources

---

## 🏗️ How RAG Works - Architecture

```
┌─────────────┐
│   User      │
│  Question   │
└──────┬──────┘
       │
       ▼
┌─────────────────┐
│  Query          │
│  Processing     │
└──────┬──────────┘
       │
       ▼
┌─────────────────┐      ┌──────────────┐
│  Vector Store   │◄─────┤  Embeddings  │
│  (Database)     │      │  Generator   │
└──────┬──────────┘      └──────────────┘
       │
       │ Search similar
       │ documents
       ▼
┌─────────────────┐
│  Retrieved      │
│  Documents      │
└──────┬──────────┘
       │
       ▼
┌─────────────────┐
│  LLM Prompt     │
│  Construction   │
└──────┬──────────┘
       │
       │ Context + Question
       ▼
┌─────────────────┐
│  LLM            │
│  (OpenAI/Claude)│
└──────┬──────────┘
       │
       ▼
┌─────────────────┐
│  Final Answer   │
│  + Citations    │
└─────────────────┘
```

---

## 📊 RAG Components Explained

### 1. **Document Store**
Where you store your knowledge base:
- PDFs, Word docs, text files
- Database records
- Web pages
- Code repositories

### 2. **Embeddings**
Convert text to numbers (vectors):
- Each document → Vector (array of numbers)
- Similar documents → Similar vectors
- Example: "What is Spring Boot?" and "Spring Boot tutorial" → Similar vectors

### 3. **Vector Database**
Stores embeddings for fast similarity search:
- Pinecone, Weaviate, Qdrant, Chroma
- MongoDB Atlas Vector Search
- PostgreSQL with pgvector

### 4. **Retrieval**
Find relevant documents:
- Semantic search (vector similarity)
- Keyword search (BM25, TF-IDF)
- Hybrid (both)

### 5. **LLM**
Generate final answer:
- OpenAI GPT-4, GPT-3.5
- Anthropic Claude
- Local models (Llama, Mistral)

---

## 🛠️ RAG Implementation Options

### Option 1: **Simple RAG (Basic)**
- Embed documents
- Store in vector DB
- Retrieve top N similar
- Send to LLM

**Best for:** Simple Q&A, small datasets

### Option 2: **Advanced RAG**
- Query rewriting
- Re-ranking results
- Multi-step retrieval
- Context compression

**Best for:** Complex queries, large datasets

### Option 3: **Hybrid RAG**
- Vector search + Keyword search
- Combines semantic + lexical matching

**Best for:** Better accuracy, diverse queries

### Option 4: **Agentic RAG**
- Multi-agent system
- Tool calling
- Iterative refinement

**Best for:** Complex workflows, multi-step tasks

---

## 💻 Node.js RAG Implementation

### Tech Stack Options:

#### **Option A: LangChain.js (Recommended)**
```bash
npm install langchain @langchain/openai @langchain/community
npm install chromadb  # or pinecone, weaviate
```

**Example:**
```javascript
import { ChatOpenAI } from "@langchain/openai";
import { Chroma } from "@langchain/community/vectorstores/chroma";
import { OpenAIEmbeddings } from "@langchain/openai/embeddings";
import { RetrievalQAChain } from "langchain/chains";
import { RecursiveCharacterTextSplitter } from "langchain/text_splitter";
import { PDFLoader } from "langchain/document_loaders/fs/pdf";

// 1. Load documents
const loader = new PDFLoader("./documents/spring-boot-guide.pdf");
const docs = await loader.load();

// 2. Split into chunks
const textSplitter = new RecursiveCharacterTextSplitter({
  chunkSize: 1000,
  chunkOverlap: 200,
});
const chunks = await textSplitter.splitDocuments(docs);

// 3. Create embeddings and vector store
const embeddings = new OpenAIEmbeddings({
  openAIApiKey: process.env.OPENAI_API_KEY,
});

const vectorStore = await Chroma.fromDocuments(chunks, embeddings, {
  collectionName: "spring-boot-docs",
});

// 4. Create RAG chain
const llm = new ChatOpenAI({
  modelName: "gpt-4",
  temperature: 0,
});

const chain = RetrievalQAChain.fromLLM(llm, vectorStore.asRetriever());

// 5. Query
const response = await chain.call({
  query: "How do I create a REST API in Spring Boot?",
});

console.log(response.text);
```

#### **Option B: LlamaIndex (Node.js)**
```bash
npm install llamaindex
```

```javascript
import { VectorStoreIndex, SimpleDirectoryReader } from "llamaindex";

// Load documents
const documents = await new SimpleDirectoryReader(
  "./documents"
).loadData();

// Create index
const index = await VectorStoreIndex.fromDocuments(documents);

// Query
const queryEngine = index.asQueryEngine();
const response = await queryEngine.query(
  "How do I create a REST API in Spring Boot?"
);

console.log(response.toString());
```

#### **Option C: Custom Implementation**
```javascript
// Using OpenAI Embeddings + Pinecone
import { OpenAI } from "openai";
import { Pinecone } from "@pinecone-database/pinecone";

const openai = new OpenAI({ apiKey: process.env.OPENAI_API_KEY });
const pinecone = new Pinecone({ apiKey: process.env.PINECONE_API_KEY });

// 1. Embed documents
async function embedDocument(text) {
  const response = await openai.embeddings.create({
    model: "text-embedding-3-small",
    input: text,
  });
  return response.data[0].embedding;
}

// 2. Store in Pinecone
async function storeDocument(id, text, metadata) {
  const embedding = await embedDocument(text);
  const index = pinecone.index("documents");
  await index.upsert([{
    id,
    values: embedding,
    metadata: { text, ...metadata },
  }]);
}

// 3. Retrieve similar
async function retrieveSimilar(query, topK = 5) {
  const queryEmbedding = await embedDocument(query);
  const index = pinecone.index("documents");
  const results = await index.query({
    vector: queryEmbedding,
    topK,
    includeMetadata: true,
  });
  return results.matches;
}

// 4. Generate answer
async function ragQuery(userQuestion) {
  // Retrieve relevant docs
  const relevantDocs = await retrieveSimilar(userQuestion);
  
  // Build context
  const context = relevantDocs
    .map(doc => doc.metadata.text)
    .join("\n\n");
  
  // Generate answer
  const completion = await openai.chat.completions.create({
    model: "gpt-4",
    messages: [
      {
        role: "system",
        content: `Answer the question using the following context:\n\n${context}`,
      },
      {
        role: "user",
        content: userQuestion,
      },
    ],
  });
  
  return {
    answer: completion.choices[0].message.content,
    sources: relevantDocs.map(doc => doc.metadata),
  };
}
```

---

## ☕ Spring Boot RAG Implementation

### Tech Stack Options:

#### **Option A: LangChain4j (Java)**
```xml
<!-- pom.xml -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId>
    <version>0.29.1</version>
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai</artifactId>
    <version>0.29.1</version>
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-embeddings-all-minilm-l6-v2</artifactId>
    <version>0.29.1</version>
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-chroma</artifactId>
    <version>0.29.1</version>
</dependency>
```

**Service Example:**
```java
@Service
public class RAGService {
    
    @Value("${openai.api.key}")
    private String openAiApiKey;
    
    private EmbeddingModel embeddingModel;
    private ChatLanguageModel chatModel;
    private EmbeddingStore<TextSegment> embeddingStore;
    
    @PostConstruct
    public void init() {
        // Embedding model
        embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        
        // Chat model
        chatModel = OpenAiChatModel.builder()
            .apiKey(openAiApiKey)
            .modelName("gpt-4")
            .temperature(0.0)
            .build();
        
        // Vector store (in-memory for demo, use Chroma/Pinecone for production)
        embeddingStore = new InMemoryEmbeddingStore<>();
    }
    
    public void ingestDocument(String documentId, String text) {
        // Split text into chunks
        DocumentSplitter splitter = new DocumentSplitters.recursive(300, 50);
        List<TextSegment> segments = splitter.split(text);
        
        // Generate embeddings
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
        
        // Store in vector database
        embeddingStore.addAll(embeddings, segments);
    }
    
    public String query(String userQuestion) {
        // Generate query embedding
        Embedding queryEmbedding = embeddingModel.embed(userQuestion).content();
        
        // Retrieve similar documents
        List<TextSegment> relevantSegments = embeddingStore.findRelevant(
            queryEmbedding, 
            5, // top 5
            0.6 // minimum score
        );
        
        // Build context
        String context = relevantSegments.stream()
            .map(TextSegment::text)
            .collect(Collectors.joining("\n\n"));
        
        // Create prompt
        String prompt = String.format(
            "Answer the following question using the provided context.\n\n" +
            "Context:\n%s\n\n" +
            "Question: %s\n\n" +
            "Answer:",
            context,
            userQuestion
        );
        
        // Generate answer
        return chatModel.generate(prompt);
    }
}
```

#### **Option B: MongoDB Atlas Vector Search**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

```java
@Service
public class MongoDBRAGService {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Value("${openai.api.key}")
    private String openAiApiKey;
    
    private OpenAIService openAIService;
    
    @PostConstruct
    public void init() {
        openAIService = new OpenAIService(openAiApiKey);
    }
    
    public void ingestDocument(String text, Map<String, Object> metadata) {
        // Generate embedding
        CreateEmbeddingRequest request = CreateEmbeddingRequest.builder()
            .model("text-embedding-3-small")
            .input(text)
            .build();
        
        CreateEmbeddingResponse response = openAIService.createEmbedding(request);
        List<Double> embedding = response.getData().get(0).getEmbedding();
        
        // Store in MongoDB
        Document doc = new Document();
        doc.put("text", text);
        doc.put("embedding", embedding);
        doc.putAll(metadata);
        doc.put("createdAt", LocalDateTime.now());
        
        mongoTemplate.insert(doc, "document_embeddings");
    }
    
    public String query(String userQuestion) {
        // Generate query embedding
        CreateEmbeddingRequest request = CreateEmbeddingRequest.builder()
            .model("text-embedding-3-small")
            .input(userQuestion)
            .build();
        
        CreateEmbeddingResponse response = openAIService.createEmbedding(request);
        List<Double> queryEmbedding = response.getData().get(0).getEmbedding();
        
        // Vector search in MongoDB
        List<Bson> pipeline = Arrays.asList(
            Aggregates.vectorSearch(
                "embedding",
                new VectorSearchOptions()
                    .filter(new BsonDocument())
                    .numCandidates(100)
                    .limit(5),
                queryEmbedding
            ),
            Aggregates.project(Projections.fields(
                Projections.include("text", "metadata"),
                Projections.computed("score", new BsonDocument("$meta", new BsonString("vectorSearchScore")))
            ))
        );
        
        List<Document> results = mongoTemplate.aggregate(
            pipeline, 
            "document_embeddings", 
            Document.class
        ).getMappedResults();
        
        // Build context
        String context = results.stream()
            .map(doc -> doc.getString("text"))
            .collect(Collectors.joining("\n\n"));
        
        // Generate answer
        ChatCompletionRequest chatRequest = ChatCompletionRequest.builder()
            .model("gpt-4")
            .messages(Arrays.asList(
                Message.builder()
                    .role("system")
                    .content("Answer using the provided context.")
                    .build(),
                Message.builder()
                    .role("user")
                    .content("Context:\n" + context + "\n\nQuestion: " + userQuestion)
                    .build()
            ))
            .build();
        
        ChatCompletionResult chatResponse = openAIService.createChatCompletion(chatRequest);
        return chatResponse.getChoices().get(0).getMessage().getContent();
    }
}
```

#### **Option C: Custom with OpenAI + PostgreSQL (pgvector)**
```java
@Service
public class CustomRAGService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Value("${openai.api.key}")
    private String openAiApiKey;
    
    private RestTemplate restTemplate = new RestTemplate();
    
    public void ingestDocument(String text, Map<String, Object> metadata) {
        // Generate embedding
        String embedding = generateEmbedding(text);
        
        // Store in PostgreSQL with pgvector
        String sql = "INSERT INTO document_embeddings (text, embedding, metadata) VALUES (?, ?::vector, ?::jsonb)";
        jdbcTemplate.update(sql, text, embedding, new Gson().toJson(metadata));
    }
    
    public String query(String userQuestion) {
        // Generate query embedding
        String queryEmbedding = generateEmbedding(userQuestion);
        
        // Vector similarity search
        String sql = "SELECT text, metadata, " +
                    "1 - (embedding <=> ?::vector) as similarity " +
                    "FROM document_embeddings " +
                    "WHERE 1 - (embedding <=> ?::vector) > 0.6 " +
                    "ORDER BY embedding <=> ?::vector " +
                    "LIMIT 5";
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(
            sql, queryEmbedding, queryEmbedding, queryEmbedding
        );
        
        // Build context and generate answer
        String context = results.stream()
            .map(r -> (String) r.get("text"))
            .collect(Collectors.joining("\n\n"));
        
        return generateAnswer(context, userQuestion);
    }
    
    private String generateEmbedding(String text) {
        // Call OpenAI embeddings API
        // Return embedding as string
    }
}
```

---

## 🎯 RAG Use Cases

### 1. **Document Q&A**
- Ask questions about company documents
- Internal knowledge base
- Technical documentation

### 2. **Customer Support**
- FAQ automation
- Product documentation
- Support ticket answers

### 3. **Code Assistant**
- Codebase Q&A
- API documentation
- Best practices

### 4. **Research Assistant**
- Academic papers
- Research documents
- Literature review

### 5. **Legal/Compliance**
- Legal documents
- Policy documents
- Compliance queries

---

## 📚 Vector Database Options

### **1. Pinecone**
- **Pros:** Managed, easy to use, fast
- **Cons:** Paid, vendor lock-in
- **Best for:** Production apps, quick setup

### **2. Weaviate**
- **Pros:** Open source, self-hosted, GraphQL
- **Cons:** Requires infrastructure
- **Best for:** Self-hosted solutions

### **3. Qdrant**
- **Pros:** Open source, fast, Rust-based
- **Cons:** Self-hosted setup
- **Best for:** Performance-critical apps

### **4. Chroma**
- **Pros:** Simple, Python-friendly
- **Cons:** Less mature
- **Best for:** Prototyping, Python projects

### **5. MongoDB Atlas Vector Search**
- **Pros:** Integrated with MongoDB, familiar
- **Cons:** MongoDB Atlas required
- **Best for:** Existing MongoDB users

### **6. PostgreSQL + pgvector**
- **Pros:** SQL, familiar, open source
- **Cons:** Less optimized for vectors
- **Best for:** SQL-based projects

---

## 🔧 Embedding Models

### **OpenAI**
- `text-embedding-3-small` - Fast, cheap
- `text-embedding-3-large` - Better quality
- `text-embedding-ada-002` - Legacy

### **Open Source**
- `all-MiniLM-L6-v2` - Fast, small
- `sentence-transformers/all-mpnet-base-v2` - Better quality
- `BGE-large-en-v1.5` - Best quality

### **Cloud Providers**
- AWS Bedrock
- Google Vertex AI
- Azure OpenAI

---

## 🎨 RAG Patterns

### **1. Naive RAG**
```
Query → Retrieve → Generate
```
Simple, works for basic use cases

### **2. Advanced RAG**
```
Query → Rewrite → Retrieve → Re-rank → Generate
```
Better accuracy, handles complex queries

### **3. Self-RAG**
```
Query → Retrieve → Generate → Evaluate → Refine
```
Iterative improvement, higher quality

### **4. Corrective RAG**
```
Query → Retrieve → Generate → Verify → Correct
```
Reduces hallucinations, fact-checking

---

## 📊 RAG Evaluation

### Metrics:
1. **Relevance** - Are retrieved docs relevant?
2. **Accuracy** - Is the answer correct?
3. **Completeness** - Does it answer fully?
4. **Latency** - Response time
5. **Cost** - API costs

### Tools:
- RAGAS (RAG Assessment)
- TruLens
- LangSmith

---

## 🚀 Best Practices

### 1. **Chunking Strategy**
- **Size:** 500-1000 tokens
- **Overlap:** 10-20% between chunks
- **Method:** Semantic boundaries (sentences, paragraphs)

### 2. **Retrieval**
- **Top-K:** 3-5 documents usually enough
- **Re-ranking:** Use cross-encoder for better results
- **Hybrid:** Combine vector + keyword search

### 3. **Prompt Engineering**
- Clear instructions
- Include context
- Specify format
- Add examples

### 4. **Monitoring**
- Track retrieval quality
- Monitor LLM costs
- Log queries and responses
- User feedback

---

## 💡 Quick Start Checklist

### Node.js:
- [ ] Install LangChain.js or LlamaIndex
- [ ] Choose vector database (Pinecone/Chroma)
- [ ] Set up OpenAI API key
- [ ] Load and chunk documents
- [ ] Create embeddings
- [ ] Store in vector DB
- [ ] Implement retrieval
- [ ] Connect to LLM
- [ ] Test queries

### Spring Boot:
- [ ] Add LangChain4j or custom implementation
- [ ] Choose vector database
- [ ] Set up OpenAI client
- [ ] Create RAG service
- [ ] Implement document ingestion
- [ ] Implement query endpoint
- [ ] Add error handling
- [ ] Test with sample documents

---

## 📖 Resources

### Documentation:
- LangChain: https://js.langchain.com/
- LangChain4j: https://github.com/langchain4j/langchain4j
- LlamaIndex: https://www.llamaindex.ai/
- Pinecone: https://www.pinecone.io/
- MongoDB Vector Search: https://www.mongodb.com/products/platform/atlas-vector-search

### Tutorials:
- LangChain RAG Tutorial
- Building RAG Applications
- Vector Databases Explained

---

## 🎯 Summary

**RAG = Search your documents + Generate answer using LLM**

**Why:** Access latest info, reduce hallucinations, domain-specific knowledge

**How:** Embed → Store → Retrieve → Generate

**Options:** LangChain, LlamaIndex, Custom

**Vector DBs:** Pinecone, Weaviate, MongoDB, PostgreSQL

**Best for:** Document Q&A, Customer Support, Code Assistants

---

**Start simple, iterate, and improve! 🚀**

