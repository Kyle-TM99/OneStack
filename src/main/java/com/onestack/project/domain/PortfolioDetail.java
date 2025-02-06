package com.onestack.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDetail {
    private int portfolioNo;
    private String portfolioTitle;
    private String portfolioContent;
    private String thumbnailImage;
    private String professionalName;  // ✅ 전문가 이름
    private String categoryTitle;     // ✅ MyBatis와 일치해야 함!
    private String selfIntroduction;
    private String contactableTime;
    private String career;
    private String awardCareer;
    private String portfolioFile1;
    private String portfolioFile2;
    private String portfolioFile3;
    private String portfolioFile4;
    private String portfolioFile5;
    private String portfolioFile6;
    private String portfolioFile7;
    private String portfolioFile8;
    private String portfolioFile9;
    private String portfolioFile10;
    private String proAnswer1;
    private String proAnswer2;
    private String proAnswer3;
    private String proAnswer4;
    private String proAnswer5;
}

