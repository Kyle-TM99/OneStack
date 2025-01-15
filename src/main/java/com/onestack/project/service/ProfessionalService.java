package com.onestack.project.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.onestack.project.domain.Portfolio;
import com.onestack.project.domain.Professional;
import com.onestack.project.domain.ProfessionalAdvancedInformation;
import com.onestack.project.mapper.ProfessionalMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfessionalService {

    @Autowired
    private ProfessionalMapper professionalMapper;

    public Integer addProfessional(Professional professional) {
        // 전문가 기본 정보 저장
        professionalMapper.addPro(professional);
        return professional.getProNo(); // 생성된 proNo 반환
    }

    public Integer addProfessionalAdvancedInfo(ProfessionalAdvancedInformation proAdvancedInfo) {
        // 전문가 고급 정보 저장
        professionalMapper.addProAdvancedInfo(proAdvancedInfo);
        return proAdvancedInfo.getProAdvancedNo(); // 생성된 proAdvancedNo 반환
    }

    public void addPortfolio(Portfolio portfolio) {
        // 포트폴리오 저장
        professionalMapper.addPortfolio(portfolio);
    }

    // 포트폴리오 파일 저장 처리
    public void savePortfolioFiles(List<String> portfolioFilePaths, Portfolio portfolio) {
        for (int i = 0; i < portfolioFilePaths.size() && i < 10; i++) {
            String fieldName = "portfolioFile" + (i + 1);
            try {
                java.lang.reflect.Field field = Portfolio.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(portfolio, portfolioFilePaths.get(i));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("포트폴리오 파일 저장 중 오류 발생", e);
            }
        }
    }



    // 파일 저장 메서드
    public String saveFile(MultipartFile file, String directory) {

        try {
            // 업로드 디렉토리 생성
            String baseDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + directory;
            File dir = new File(baseDir);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("디렉토리를 생성할 수 없습니다: " + baseDir);
            }

            if (!dir.canWrite()) {
                throw new IOException("디렉토리에 쓰기 권한이 없습니다: " + baseDir);
            }

            // 고유한 파일 이름 생성
            String sanitizedFileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
            String uniqueFileName = UUID.randomUUID().toString() + "_" + sanitizedFileName;

            // 파일 저장
            try (InputStream inputStream = file.getInputStream()) {
                Path targetPath = Paths.get(baseDir, uniqueFileName);
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("파일 저장 성공: {}", targetPath);
                return targetPath.toString();
            }
        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
        }
    }
    
    

    public List<String> saveFiles(Map<String, MultipartFile> files, String directory) {
        return files.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .map(entry -> saveFile(entry.getValue(), directory))
                .collect(Collectors.toList());
    }
    
}