package com.onestack.project.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

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
    public void savePortfolioFiles(Map<String, MultipartFile> portfolioFiles, Portfolio portfolio) {
        int fileCount = 1;
        for (Map.Entry<String, MultipartFile> entry : portfolioFiles.entrySet()) {
            if (fileCount > 10) {
                break; // 최대 10개 파일까지만 처리
            }
            
            String filePath = saveFile(entry.getValue(), "portfolio"); // 파일 저장
            String fieldName = "portfolioFile" + fileCount;

            // 해당 필드에 파일 경로 저장
            try {
                java.lang.reflect.Field field = Portfolio.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(portfolio, filePath);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("파일 경로 저장 오류", e);
            }

            fileCount++;
        }
    }

    // 파일 저장 메서드
    private String saveFile(MultipartFile file, String directory) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String baseDir = System.getProperty("user.dir") + "/uploads/" + directory;
            File dir = new File(baseDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String filePath = baseDir + "/" + uniqueFileName;
            file.transferTo(new File(filePath));
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
        }
    }
}