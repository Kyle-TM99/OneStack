package com.onestack.project.controller;

import com.onestack.project.dto.ImageDto;
import com.onestack.project.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    // 이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (!isValidImageFile(file)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "지원하지 않는 파일 형식입니다."));
            }

            String imageUrl = imageService.uploadImage(file);
            return ResponseEntity.ok(Map.of("url", imageUrl));

        } catch (IOException e) {
            log.error("파일 업로드 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "파일 업로드에 실패했습니다."));
        }
    }

    // 이미지 삭제
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Map<String, String>> deleteImage(@PathVariable String fileName) {
        try {
            imageService.deleteImage(fileName);
            return ResponseEntity.ok(Map.of("message", "파일이 삭제되었습니다."));

        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();

        } catch (IOException e) {
            log.error("파일 삭제 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "파일 삭제에 실패했습니다."));
        }
    }

    // 모든 이미지 목록 조회
    @GetMapping
    public ResponseEntity<List<ImageDto>> getAllImages() {
        try {
            List<ImageDto> images = imageService.getAllImages();
            return ResponseEntity.ok(images);

        } catch (IOException e) {
            log.error("이미지 목록 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 특정 이미지 정보 조회
    @GetMapping("/{fileName}")
    public ResponseEntity<ImageDto> getImageInfo(@PathVariable String fileName) {
        try {
            ImageDto imageDto = imageService.getImageInfo(fileName);
            return ResponseEntity.ok(imageDto);

        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();

        } catch (IOException e) {
            log.error("이미지 정보 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}