package com.onestack.project.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProConversionRequest {
    private Integer memberNo;
    private int itemNo;
    private String selfIntroduction;
    private List<String> carrer;
    private List<String> awardCarrer;
    private List<Map<String, String>> proAnswers = new ArrayList<>();
    private String portfolioTitle;
    private String portfolioContent;
    private MultipartFile thumbnailImage;
    private List<MultipartFile> portfolioFiles;
}
