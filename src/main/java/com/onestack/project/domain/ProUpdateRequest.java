package com.onestack.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProUpdateRequest {
    private int proNo;                // 전문가 PK 추가
    private int proAdvancedNo;        // 전문가 고급정보 PK 추가
    private int portfolioNo;          // 포트폴리오 PK 추가
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
}
