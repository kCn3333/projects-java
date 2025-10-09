package com.kcn.e_shop.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
public class FileUploadUtil {

    private static final String UPLOAD_DIR = "uploads/products/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    public static String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty.");
        }

        // 1. Walidacja rozmiaru
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File is too large. Max size is 5 MB.");
        }

        // 2. Walidacja typu MIME
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/png") || contentType.equals("image/jpeg"))) {
            throw new IOException("Invalid file type. Only PNG and JPEG are allowed.");
        }

        // 3. Tworzymy katalog jeśli nie istnieje
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 4. Generujemy unikalną nazwę pliku
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // 5. Zapis pliku
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("[File] /" + UPLOAD_DIR + fileName + " saved successfully");

        return "/" + UPLOAD_DIR + fileName; // zwracamy ścieżkę do zapisania w bazie
    }
}
