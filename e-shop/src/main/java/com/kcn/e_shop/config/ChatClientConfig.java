package com.kcn.e_shop.config;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ChatClientConfig {

//    @Bean
//    public ChatClient chatClient(ChatModel chatModel){
//        return ChatClient.create(chatModel);
//    }

    @Bean
    public VectorStore vectorStore(@Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel, JdbcTemplate jdbcTemplate) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel).build();
    }

    @Bean
    @Primary
    public OllamaEmbeddingModel ollamaEmbeddingModel(OllamaApi ollamaApi) {
        OllamaOptions options = OllamaOptions.builder()
                .model("mxbai-embed-large:latest")
                .build();

        ModelManagementOptions managementOptions = ModelManagementOptions.builder()
                .build();

        return new OllamaEmbeddingModel(
                ollamaApi,
                options,
                ObservationRegistry.NOOP,
                managementOptions
        );
    }

    @Bean
    public ChatClient chatClient(OllamaChatModel chatModel){
        return ChatClient.create(chatModel);
    }
}