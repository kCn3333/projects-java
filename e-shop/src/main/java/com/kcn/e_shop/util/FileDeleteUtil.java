package com.kcn.e_shop.util;

import com.kcn.e_shop.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileDeleteUtil {

    private FileDeleteUtil() {}

    public static void deleteFileIfExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) return;

        Path path = Paths.get(filePath);
        try {
            Files.deleteIfExists(path);
            log.info("[File] " + filePath + " deleted successfully");
        } catch (IOException e) {
            throw new FileStorageException("Failed to delete file: " + filePath, e);
        }
    }
}
