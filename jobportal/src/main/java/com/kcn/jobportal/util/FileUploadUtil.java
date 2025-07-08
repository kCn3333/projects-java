package com.kcn.jobportal.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        // Walidacja wejścia
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IOException("Plik jest pusty lub null: " + fileName);
        }

        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IOException("Nazwa pliku jest pusta");
        }

        // Czyścimy nazwę pliku z potencjalnie niebezpiecznych znaków
        fileName = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");

        // Tworzymy ścieżkę do katalogu
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
                System.out.println(">>> Directory created: " + uploadPath.toAbsolutePath());
            } catch (IOException e) {
                throw new IOException("Nie można utworzyć katalogu: " + uploadPath.toAbsolutePath(), e);
            }
        }

        // Zapisujemy plik
        Path filePath = uploadPath.resolve(fileName);
        System.out.println(">>> Saving file to: " + filePath);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Nie udało się zapisać pliku: " + filePath.toAbsolutePath(), e);
        }
    }




}
