package com.onestack.project.domain;

import java.util.List;

import lombok.Data;

@Data
public class ProConversionRequest {
	private int memberNo;
    private int itemNo;
    private String selfIntroduction;
    private String contactableTimeStart;
    private String contactableTimeEnd;
    private List<String> carrer;
    private List<String> awardCarrer;
    private List<String> surveyAnswers;
    private String portfolioTitle;
    private String portfolioContent;
    private String thumbnailImage;
    private List<String> portfolioFilePaths;
}

