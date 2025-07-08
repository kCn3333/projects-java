package com.kcn.jobportal.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class FileDownloadUtil {

    public Resource getFileAsResource(String downloadDirectory, String fileName) throws IOException {
        Path dirPath = Paths.get(downloadDirectory);

        try (Stream<Path> stream = Files.list(dirPath)) {
            Optional<Path> foundFile = stream
                    .filter(file -> file.getFileName().toString().startsWith(fileName))
                    .findFirst();

            if (foundFile.isPresent()) {
                return new UrlResource(foundFile.get().toUri());
            }
        }

        return null;
    }
}

