package com.onestack.project.service;

import java.util.List;

import com.onestack.project.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onestack.project.mapper.ProfessionalMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Slf4j
public class ProfessionalService {

    @Autowired
    private ProfessionalMapper professionalMapper;

    // 심사요청 시 각각 데이터 전문가/전문가고급정보/포트폴리오 테이블에 저장
    public void saveProConversionData(ProConversionRequest request) {

        professionalMapper.updateMemberType(2, request.getMemberNo());

        // 전문가 저장
        Professional professional = new Professional();
        professional.setMemberNo(request.getMemberNo());
        professional.setCategoryNo(request.getCategoryNo());
        professional.setSelfIntroduction(request.getSelfIntroduction());
        professional.setCareer(String.join(",", request.getCareer()));
        professional.setAwardCareer(String.join(",", request.getAwardCareer()));
        professional.setContactableTime(request.getContactableTimeStart() + " - " + request.getContactableTimeEnd());

        professionalMapper.addPro(professional);
        int proNo = professional.getProNo();
        
        // 전문가 고급정보 저장
        ProfessionalAdvancedInformation advancedInfo = new ProfessionalAdvancedInformation();
        advancedInfo.setProNo(proNo);
        advancedInfo.setItemNo(request.getItemNo());
        List<String> surveyAnswers = request.getSurveyAnswers();
        advancedInfo.setProAnswer1(surveyAnswers.get(0));
        advancedInfo.setProAnswer2(surveyAnswers.size() > 1 ? surveyAnswers.get(1) : null);
        advancedInfo.setProAnswer3(surveyAnswers.size() > 2 ? surveyAnswers.get(2) : null);
        advancedInfo.setProAnswer4(surveyAnswers.size() > 3 ? surveyAnswers.get(3) : null);
        advancedInfo.setProAnswer5(surveyAnswers.size() > 4 ? surveyAnswers.get(4) : null);
        

        professionalMapper.addProAdvancedInfo(advancedInfo);
        int proAdvancedNo = advancedInfo.getProAdvancedNo();

        final String IMAGE_BASE_URL = "http://3.37.88.97/images/";

        String thumbnailUrl = request.getThumbnailImage();
        if (thumbnailUrl != null && !thumbnailUrl.startsWith("http")) {
            thumbnailUrl = IMAGE_BASE_URL + thumbnailUrl;
        }

        // 포폴 저장
        Portfolio portfolio = new Portfolio();
        portfolio.setProNo(proNo);
        portfolio.setProAdvancedNo(proAdvancedNo);
        portfolio.setPortfolioTitle(request.getPortfolioTitle());
        portfolio.setPortfolioContent(request.getPortfolioContent());
        portfolio.setVisibility(true); // 공개       
        portfolio.setThumbnailImage(thumbnailUrl);
        List<String> portfolioFilePaths = request.getPortfolioFilePaths();
        portfolio.setPortfolioFile1(portfolioFilePaths.get(0));
        portfolio.setPortfolioFile2(portfolioFilePaths.size() > 1 ? portfolioFilePaths.get(1) : null);
        portfolio.setPortfolioFile3(portfolioFilePaths.size() > 2 ? portfolioFilePaths.get(2) : null);
        portfolio.setPortfolioFile4(portfolioFilePaths.size() > 3 ? portfolioFilePaths.get(3) : null);
        portfolio.setPortfolioFile5(portfolioFilePaths.size() > 4 ? portfolioFilePaths.get(4) : null);
        portfolio.setPortfolioFile6(portfolioFilePaths.size() > 5 ? portfolioFilePaths.get(5) : null);
        portfolio.setPortfolioFile7(portfolioFilePaths.size() > 6 ? portfolioFilePaths.get(6) : null);
        portfolio.setPortfolioFile8(portfolioFilePaths.size() > 7 ? portfolioFilePaths.get(7) : null);
        portfolio.setPortfolioFile9(portfolioFilePaths.size() > 8 ? portfolioFilePaths.get(8) : null);
        portfolio.setPortfolioFile10(portfolioFilePaths.size() > 9 ? portfolioFilePaths.get(9) : null);

        professionalMapper.addPortfolio(portfolio);
    }

    /* 전문가 정보 조회 */
    public List<MemberWithProfessional> getPro2(int proNo) {
        return professionalMapper.getPro2(proNo);
    }

    public List<Portfolio> getPortfoliosByMember(int memberNo) {
        return professionalMapper.getPortfoliosByMember(memberNo);
    }

}