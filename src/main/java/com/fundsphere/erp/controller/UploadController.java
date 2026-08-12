package com.fundsphere.erp.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadController {

    private final Path uploadDirectory =
        Paths.get("uploads")
             .toAbsolutePath()
             .normalize();

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getImage(
            @PathVariable String filename) {

        try {

            Path filePath = uploadDirectory
                    .resolve(filename)
                    .normalize();

            if (!filePath.startsWith(uploadDirectory)) {
                return ResponseEntity.badRequest().build();
            }

            Resource resource =
                    new FileSystemResource(filePath);

            if (!resource.exists() ||
                !resource.isReadable()) {

                return ResponseEntity.notFound().build();
            }

            String contentType =
                    getContentType(filename);

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + filename + "\""
                    )
                    .contentType(
                            MediaType.parseMediaType(contentType)
                    )
                    .body(resource);

        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }
    }

    private String getContentType(String filename) {

        String lower =
                filename.toLowerCase();

        if (lower.endsWith(".png")) {
            return "image/png";
        }

        if (lower.endsWith(".jpg") ||
            lower.endsWith(".jpeg")) {
            return "image/jpeg";
        }

        if (lower.endsWith(".gif")) {
            return "image/gif";
        }

        if (lower.endsWith(".webp")) {
            return "image/webp";
        }

        return "application/octet-stream";
    }
}