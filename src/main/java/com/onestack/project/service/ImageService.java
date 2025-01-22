package com.onestack.project.service;

import com.onestack.project.dto.ImageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ImageService {

    private final String IMAGE_DIRECTORY = "/var/www/images/";
    private final String IMAGE_BASE_URL = "http://3.37.88.97/images/";

    // 이미지 업로드
    public String uploadImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;

        Path filePath = Paths.get(IMAGE_DIRECTORY + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return IMAGE_BASE_URL + fileName;
    }

    // 이미지 삭제
    public void deleteImage(String fileName) throws IOException {
        Path filePath = Paths.get(IMAGE_DIRECTORY + fileName);

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + fileName);
        }

        Files.delete(filePath);
    }

    // 모든 이미지 목록 조회
    public List<ImageDto> getAllImages() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(IMAGE_DIRECTORY))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> isImageFile(path.toString()))
                    .map(this::createImageDto)
                    .collect(Collectors.toList());
        }
    }

    // 특정 이미지 정보 조회
    public ImageDto getImageInfo(String fileName) throws IOException {
        Path filePath = Paths.get(IMAGE_DIRECTORY + fileName);

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + fileName);
        }

        return createImageDto(filePath);
    }

    private ImageDto createImageDto(Path path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            String fileName = path.getFileName().toString();

            return ImageDto.builder()
                    .fileName(fileName)
                    .url(IMAGE_BASE_URL + fileName)
                    .size(Files.size(path))
                    .createdAt(attrs.creationTime().toInstant())
                    .modifiedAt(attrs.lastModifiedTime().toInstant())
                    .build();
        } catch (IOException e) {
            log.error("이미지 정보 읽기 실패: {}", path, e);
            return null;
        }
    }

    private boolean isImageFile(String path) {
        String lowercasePath = path.toLowerCase();
        return lowercasePath.endsWith(".jpg") ||
                lowercasePath.endsWith(".jpeg") ||
                lowercasePath.endsWith(".png") ||
                lowercasePath.endsWith(".gif") ||
                lowercasePath.endsWith(".webp");
    }
}