package org.fantasymaps.backend.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implementation is created for MapService to convert an image file to MultipartFile
 */
public class ConvertMultipartFile implements MultipartFile {
    private final Path path;
    private final String contentType;

    public ConvertMultipartFile(Path path, String contentType){
        this.path=path;
        this.contentType = contentType;
    }
    @Override
    public String getName(){
        return path.getFileName().toString();
    }
    @Override
    public String getOriginalFilename() {
        return getName();
    }
    @Override
    public String getContentType() {
        return contentType;
    }
    @Override
    public long getSize() {
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean isEmpty() {
        return getSize()==0;
    }
    @Override
    public byte[] getBytes() throws IOException {
        return Files.readAllBytes(path);
    }
    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(path);
    }
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        transferTo(dest.toPath());
    }
    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {
        Files.copy(path, dest);
    }
}
