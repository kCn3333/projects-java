package com.kcn.e_shop.service;

import com.kcn.e_shop.dto.ProductDTO;
import com.kcn.e_shop.exception.EmbeddingOperationException;
import com.kcn.e_shop.mapper.ProductMapper;
import com.kcn.e_shop.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductEmbeddingService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;


    @PostConstruct
    public void init() {
        List<ProductDTO> products = productRepository.findAllWithCategory()
                .stream()
                .map(productMapper::toDTO)
                .toList();
        addAllProductsToVectorStore(products); // jeśli poleci wyjątek → aplikacja nie wystartuje
        log.info("[VectorStore] Product embeddings successfully initialized with {} products", products.size());
    }

    public void addAllProductsToVectorStore(List<ProductDTO> products) {
        List<Document> docs = new ArrayList<>();

        for (ProductDTO p : products) {
            String content = p.getName() + ". " + p.getDescription() + ". " + p.getFeatures();

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("price", p.getPrice());
            metadata.put("category", p.getCategoryName());

            Document doc = Document.builder()
                    .id(p.getId().toString())  // UUID w formacie string
                    .text(content)
                    .metadata(metadata)
                    .build();

            docs.add(doc);
        }

        try {
            vectorStore.add(docs);
        } catch (Exception e) {
            throw new EmbeddingOperationException("Failed to add products to vector store", e);
        }
    }

    public void addProductToVectorStore(ProductDTO product) {
        addAllProductsToVectorStore(List.of(product));
    }

    public void removeProductFromVectorStore(UUID productId) {
        try {
            String documentId = productId.toString();
            int rows = jdbcTemplate.update("DELETE FROM vector_store WHERE id = ?", documentId);

            if (rows == 0) {
                throw new EmbeddingOperationException("No document found in vector store with ID: " + documentId);
            }
        } catch (Exception e) {
            throw new EmbeddingOperationException("Error removing product from vector store", e);
        }
    }

    public void updateProductInVectorStore(ProductDTO product) {
        try {
            removeProductFromVectorStore(product.getId());
            addProductToVectorStore(product);
            log.info("[VectorStore] Updated vector for product: {}", product.getName());
        } catch (Exception e) {
            throw new EmbeddingOperationException("Failed to update product in vector store", e);
        }
    }


}
