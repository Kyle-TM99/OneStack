package com.onestack.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class PortfolioController {

    // 파일 저장 경로 설정
    private static final String UPLOAD_DIR = "C:/uploads/";

    @PostMapping("/portfolio/upload")
    public ResponseEntity<Map<String, Object>> uploadFiles(
            @RequestParam("thumbnailImage") MultipartFile thumbnailImage,
            @RequestParam(value = "portfolioFiles", required = false) MultipartFile[] portfolioFiles) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 저장 폴더 생성
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 1. 썸네일 이미지 저장
            if (thumbnailImage.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "썸네일 파일이 비어 있습니다."));
            }
            String thumbnailFilename = saveFile(thumbnailImage);

            // 2. 포트폴리오 파일 저장
            List<String> portfolioFilePaths = new ArrayList<>();
            if (portfolioFiles != null) {
                for (MultipartFile file : portfolioFiles) {
                    if (!file.isEmpty()) {
                        String portfolioFilename = saveFile(file);
                        portfolioFilePaths.add(portfolioFilename);
                    }
                }
            }

            // 응답 데이터 구성
            response.put("thumbnailImage", "/uploads/" + thumbnailFilename);
            response.put("portfolioFiles", portfolioFilePaths);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "파일 업로드 중 오류 발생"));
        }
    }

    // 파일 저장 메서드
    private String saveFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String filePath = UPLOAD_DIR + originalFilename;

        file.transferTo(new File(filePath));
        return originalFilename; // 저장된 파일 이름 반환
    }
}
