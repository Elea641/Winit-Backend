package com.cda.winit.shared.domain.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface IImageUploadService {
    String generateImageUrlAndSaveImage(MultipartFile file) throws IOException;

    String generateUUID();

    Path getPath(String UUID, MultipartFile file);

    void saveImage(Path path, MultipartFile file) throws IOException;

    String cleanOriginalFileName(String originalFileName);
}