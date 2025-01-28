package com.onestack.project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ImageService {

    private final String IMAGE_DIRECTORY = "/usr/share/nginx/html/images/";
    private final String IMAGE_BASE_URL = "http://3.37.88.97/images/";

    public String uploadImage(MultipartFile file) throws IOException {
        // 파일명 생성 (UUID 사용)
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;

        // 파일 저장 경로
        Path filePath = Paths.get(IMAGE_DIRECTORY + fileName);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return IMAGE_BASE_URL + fileName;
    }

    public void deleteImage(String fileName) throws IOException {
        Path filePath = Paths.get(IMAGE_DIRECTORY + fileName);
        Files.delete(filePath);
    }

    public List<String> getAllImages() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(IMAGE_DIRECTORY))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> IMAGE_BASE_URL + path.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }
}