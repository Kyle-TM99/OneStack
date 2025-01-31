package com.onestack.project.controller;

import com.onestack.project.domain.Portfolio;
import com.onestack.project.service.ProfessionalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.StandardCopyOption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class PortfolioController {


    private final String IMAGE_DIRECTORY = "/usr/share/nginx/html/images/";
    private final String IMAGE_BASE_URL = "http://3.37.88.97/images/";

    @PostMapping("/portfolio/upload")
    public ResponseEntity<Map<String, Object>> uploadFiles(
            @RequestParam("thumbnailImage") MultipartFile thumbnailImage,
            @RequestParam(value = "portfolioFiles", required = false) MultipartFile[] portfolioFiles) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 디렉토리가 존재하지 않으면 생성
            Path uploadPath = Paths.get(IMAGE_DIRECTORY);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 썸네일 이미지 저장
            if (thumbnailImage.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "썸네일 파일이 비어 있습니다."));
            }
            String thumbnailUrl = uploadImage(thumbnailImage);

            // 포트폴리오 파일 저장
            List<String> portfolioFileUrls = new ArrayList<>();
            if (portfolioFiles != null) {
                for (MultipartFile file : portfolioFiles) {
                    if (!file.isEmpty()) {
                        String portfolioUrl = uploadImage(file);
                        portfolioFileUrls.add(portfolioUrl);
                    }
                }
            }

            response.put("thumbnailImage", thumbnailUrl);
            response.put("portfolioFiles", portfolioFileUrls);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "파일 업로드 중 오류 발생"));
        }
    }

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

