package com.onestack.project.domain;

import java.util.List;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProConversionRequest {
	private int memberNo;
    private int itemNo;
    private int categoryNo;
    private String selfIntroduction;
    private String contactableTimeStart;
    private String contactableTimeEnd;
    private List<String> career;
    private List<String> awardCareer;
    private List<String> surveyAnswers;
    private String portfolioTitle;
    private String portfolioContent;
    private String thumbnailImage;
    private List<String> portfolioFilePaths;

    private MultipartFile thumbnailImageFile; // 썸네일 파일
    private List<MultipartFile> portfolioFiles; // 포트폴리오 파일들
}

