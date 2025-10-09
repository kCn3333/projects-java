package com.kcn.e_shop.service;

import com.kcn.e_shop.dto.ProductDTO;
import com.kcn.e_shop.entity.Category;
import com.kcn.e_shop.entity.Product;
import com.kcn.e_shop.exception.FileStorageException;
import com.kcn.e_shop.exception.ProductDeletionException;
import com.kcn.e_shop.mapper.ProductMapper;
import com.kcn.e_shop.repository.CategoryRepository;
import com.kcn.e_shop.repository.ProductRepository;
import com.kcn.e_shop.util.FileDeleteUtil;
import com.kcn.e_shop.util.FileUploadUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductEmbeddingService productEmbeddingService;
    private final ProductMapper productMapper;
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("${spring.custom.search.top-k}")
    private int topK;


    public ProductDTO findById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        return productMapper.toDTO(product);
    }

    @Transactional
    public void saveOrUpdate(ProductDTO dto, MultipartFile imageFile) {
        // Validate category
        Category category = categoryRepository.findByName(dto.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + dto.getCategoryName()));

        Product product;
        if (dto.getId() != null) {
            // UPDATE EXISTING PRODUCT
            product = productRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + dto.getId()));

            // Update fields from DTO
            productMapper.updateEntityFromDTO(dto, product, category);

            // Handle image for UPDATE: only update if new file provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String newImagePath = saveImageFile(imageFile);
                product.setImagePath(newImagePath);
            }
            // If no new image, keep the existing imagePath

        } else {
            // CREATE NEW PRODUCT
            product = productMapper.toEntity(dto, category);

            // Handle image for NEW PRODUCT
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImageFile(imageFile);
                product.setImagePath(imagePath);
            } else {
                product.setImagePath(null); // No image for new product
            }
        }

        Product saved = productRepository.save(product);
        productEmbeddingService.addProductToVectorStore(productMapper.toDTO(saved));
        log.info("[Product] " + saved.getName() + " saved successfully");
    }
    private String saveImageFile(MultipartFile imageFile) {
        try {
            return FileUploadUtil.saveFile(imageFile);
        } catch (IOException e) {
            throw new FileStorageException("Failed to store product image", e);
        }
    }

    @Transactional
    public void deleteById(UUID id) {
        Product toDelete = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        try {
            productRepository.deleteById(id);
            productRepository.flush();
            FileDeleteUtil.deleteFileIfExists(toDelete.getImagePath());
            productEmbeddingService.removeProductFromVectorStore(id);
            log.info("[Product] " + toDelete.getName() + " deleted successfully");
        } catch (DataIntegrityViolationException e) {
            throw new ProductDeletionException("Cannot delete product: it is part of existing orders.", id);
        }
    }

    public List<ProductDTO> findAll() {

        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getUpdatedAt).reversed())
                .map(productMapper::toDTO)
                .toList();
    }


    public String generateDescription(String productName, String categoryName) {
        String prompt = String.format(
                """
                Write a single, professional product description for the %s product named '%s'.\s
                The description must be one sentence only, up to 50 characters,\s
                highlight the main benefit, and be optimized for e-commerce listings. Output plain text only.
               \s""",
                categoryName, productName
        );
        String description;
        try {
            ChatResponse chatResponse = chatClient.prompt(prompt)
                    .call()
                    .chatResponse();
            log.info("[" + chatResponse.getMetadata().getModel() + "] Generating description for: " + productName + " from " + categoryName + " category");
            description = chatResponse.getResult().getOutput().getText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate description: " + e.getMessage(), e);

        }

        return description;

    }

    public String generateFeatures(String productName, String categoryName) {
        String prompt = String.format(
                """
                Produce exactly 5–7 features for '%s' (%s). Output must be one line,\s
                features separated by commas, no numbering, no bullets, no extra text. Keep within 200 characters."
                       \s""",
                categoryName, productName
        );
        String features;
        try {
            ChatResponse chatResponse = chatClient.prompt(prompt)
                    .call()
                    .chatResponse();
            log.info("[" + chatResponse.getMetadata().getModel() + "] Generating features for: " + productName + " from " + categoryName + " category");
            features = chatResponse.getResult().getOutput().getText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate description: " + e.getMessage(), e);

        }

        return features;


    }

    public List<ProductDTO> search(String query) {

        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(topK)
                        .build()
        );
        List<UUID> ids = results.stream()
                .map(doc -> UUID.fromString(doc.getId()))
                .toList();

        List<Product> found = productRepository.findAllById(ids);
        return found.stream()
                .sorted(Comparator.comparing(Product::getUpdatedAt).reversed())
                .map(productMapper::toDTO)
                .toList();
    }


}