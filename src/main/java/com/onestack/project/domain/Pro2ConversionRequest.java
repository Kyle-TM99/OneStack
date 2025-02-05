package com.onestack.project.domain;

import java.util.List;

import lombok.Data;

@Data
public class Pro2ConversionRequest {
    private int proNo;
    private int itemNo;
    private int proAdvancedNo;
    private List<String> surveyAnswers;
    private String portfolioTitle;
    private String portfolioContent;
    private String thumbnailImage;
    private List<String> portfolioFilePaths;
}