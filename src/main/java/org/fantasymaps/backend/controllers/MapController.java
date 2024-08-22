package org.fantasymaps.backend.controllers;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/map")
public class MapController {
    @PostMapping
    public ResponseEntity<String> uploadMap(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
            } else if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
                // TODO specify allowed file types
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File must be an image");
            }

            String fileName = file.getOriginalFilename();
            InputStream content = file.getInputStream();

            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.create(fileName, content, file.getContentType());

            String fileUrl = blob.getMediaLink();
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }
}
