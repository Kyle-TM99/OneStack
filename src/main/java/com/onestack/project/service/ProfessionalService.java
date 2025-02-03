package com.onestack.project.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.onestack.project.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.onestack.project.mapper.ProfessionalMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
public class ProfessionalService {

    @Autowired
    private ProfessionalMapper professionalMapper;

    private final String IMAGE_DIRECTORY = "/usr/share/nginx/html/images/";
    private final String IMAGE_BASE_URL = "http://3.37.88.97/images/";

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

    // 포트폴리오 리스트 조회
    public List<Portfolio> getPortfoliosByMember(int memberNo) {
        return professionalMapper.getPortfoliosByMember(memberNo);
    }

    public List<String> getPortfolioFiles(int portfolioNo) {
        Map<String, Object> result = professionalMapper.getPortfolioFiles(portfolioNo);
        List<String> files = new ArrayList<>();

        if (result != null) {
            for (int i = 1; i <= 10; i++) {
                String fileKey = "portfolio_file" + i;
                if (result.containsKey(fileKey) && result.get(fileKey) != null) {
                    files.add(result.get(fileKey).toString());
                }
            }
        }
        return files;
    }



    public void deletePortfolio(int portfolioNo) {
        // ✅ 포트폴리오 번호로 관련 전문가 및 전문가 고급 정보 조회
        ProfessionalInfo professionalInfo = professionalMapper.getProfessionalInfoByPortfolio(portfolioNo);

        if (professionalInfo != null) {
            int proNo = professionalInfo.getProNo();
            int proAdvancedNo = professionalInfo.getProAdvancedNo();

            // ✅ 1. 포트폴리오 삭제
            professionalMapper.deletePortfolio(portfolioNo);

            // ✅ 2. 전문가 고급 정보 삭제
            professionalMapper.deleteProfessionalAdvancedInfo(proAdvancedNo);

            // ✅ 3. 전문가 삭제 (해당 전문가가 마지막 포트폴리오일 경우만 삭제)
            professionalMapper.deleteProfessional(proNo);
        }
    }


    // ✅ PortfolioDetail을 Portfolio로 변환하는 메서드
    private Portfolio convertToPortfolio(PortfolioDetail detail) {
        if (detail == null) return null;

        Portfolio portfolio = new Portfolio();
        portfolio.setPortfolioNo(detail.getPortfolioNo());
        portfolio.setPortfolioTitle(detail.getPortfolioTitle());
        portfolio.setPortfolioContent(detail.getPortfolioContent());
        portfolio.setThumbnailImage(detail.getThumbnailImage());

        // ✅ 포트폴리오 파일 정보 복사
        portfolio.setPortfolioFile1(detail.getPortfolioFile1());
        portfolio.setPortfolioFile2(detail.getPortfolioFile2());
        portfolio.setPortfolioFile3(detail.getPortfolioFile3());
        portfolio.setPortfolioFile4(detail.getPortfolioFile4());
        portfolio.setPortfolioFile5(detail.getPortfolioFile5());
        portfolio.setPortfolioFile6(detail.getPortfolioFile6());
        portfolio.setPortfolioFile7(detail.getPortfolioFile7());
        portfolio.setPortfolioFile8(detail.getPortfolioFile8());
        portfolio.setPortfolioFile9(detail.getPortfolioFile9());
        portfolio.setPortfolioFile10(detail.getPortfolioFile10());

        return portfolio;
    }

    // ✅ 전문가 정보 조회
    public Professional getProfessionalByPortfolio(int portfolioNo) {
        return professionalMapper.getProfessionalByPortfolio(portfolioNo);
    }

    // ✅ 포트폴리오 조회 (PortfolioDetail -> Portfolio 변환)
    public Portfolio getPortfolioById(int portfolioNo) {
        PortfolioDetail portfolio = professionalMapper.getPortfolioById(portfolioNo);
        return convertToPortfolio(portfolio);
    }

    // ✅ 전문가 고급 정보 조회
    public ProfessionalAdvancedInformation getAdvancedInfoByPortfolio(int portfolioNo) {
        return professionalMapper.getAdvancedInfoByPortfolio(portfolioNo);
    }

    @Transactional
    public void updateProConversionData(ProUpdateRequest request) {
        // ✅ 기존 포트폴리오 정보 가져오기
        Portfolio existingPortfolio = professionalMapper.getPortfolioEntityById(request.getPortfolioNo());
        if (existingPortfolio == null) {
            throw new RuntimeException("포트폴리오 데이터를 찾을 수 없습니다.");
        }

        // ✅ 포트폴리오 업데이트
        Portfolio portfolio = new Portfolio();
        portfolio.setPortfolioNo(request.getPortfolioNo());
        portfolio.setProNo(request.getProNo());
        portfolio.setProAdvancedNo(request.getProAdvancedNo());
        portfolio.setPortfolioTitle(request.getPortfolioTitle());
        portfolio.setPortfolioContent(request.getPortfolioContent());

        // ✅ 썸네일 이미지 유지 (새로운 파일이 없으면 기존 이미지 유지)
        String updatedThumbnail = request.getThumbnailImage();
        if (updatedThumbnail == null || updatedThumbnail.isEmpty()) {
            updatedThumbnail = existingPortfolio.getThumbnailImage();
        } else if (!updatedThumbnail.startsWith("http")) {
            updatedThumbnail = IMAGE_BASE_URL + updatedThumbnail;
        }
        portfolio.setThumbnailImage(updatedThumbnail);

        // ✅ 포트폴리오 파일 유지 (새로운 파일이 없으면 기존 파일 유지)
        List<String> filePaths = request.getPortfolioFilePaths();
        portfolio.setPortfolioFile1(filePaths.size() > 0 ? filePaths.get(0) : existingPortfolio.getPortfolioFile1());
        portfolio.setPortfolioFile2(filePaths.size() > 1 ? filePaths.get(1) : existingPortfolio.getPortfolioFile2());
        portfolio.setPortfolioFile3(filePaths.size() > 2 ? filePaths.get(2) : existingPortfolio.getPortfolioFile3());
        portfolio.setPortfolioFile4(filePaths.size() > 3 ? filePaths.get(3) : existingPortfolio.getPortfolioFile4());
        portfolio.setPortfolioFile5(filePaths.size() > 4 ? filePaths.get(4) : existingPortfolio.getPortfolioFile5());
        portfolio.setPortfolioFile6(filePaths.size() > 5 ? filePaths.get(5) : existingPortfolio.getPortfolioFile6());
        portfolio.setPortfolioFile7(filePaths.size() > 6 ? filePaths.get(6) : existingPortfolio.getPortfolioFile7());
        portfolio.setPortfolioFile8(filePaths.size() > 7 ? filePaths.get(7) : existingPortfolio.getPortfolioFile8());
        portfolio.setPortfolioFile9(filePaths.size() > 8 ? filePaths.get(8) : existingPortfolio.getPortfolioFile9());
        portfolio.setPortfolioFile10(filePaths.size() > 9 ? filePaths.get(9) : existingPortfolio.getPortfolioFile10());

        professionalMapper.updatePortfolio(portfolio);

        // ✅ 전문가 정보 업데이트
        Professional professional = new Professional();
        professional.setProNo(request.getProNo());
        professional.setCategoryNo(request.getCategoryNo());
        professional.setSelfIntroduction(request.getSelfIntroduction());
        professional.setCareer(String.join(",", request.getCareer()));
        professional.setAwardCareer(String.join(",", request.getAwardCareer()));
        professional.setContactableTime(request.getContactableTimeStart() + " - " + request.getContactableTimeEnd());
        professionalMapper.updateProfessional(professional);

        // ✅ 전문가 고급 정보 업데이트
        ProfessionalAdvancedInformation advancedInfo = new ProfessionalAdvancedInformation();
        advancedInfo.setProAdvancedNo(request.getProAdvancedNo());
        advancedInfo.setProNo(request.getProNo());
        advancedInfo.setItemNo(request.getItemNo());

        List<String> surveyAnswers = request.getSurveyAnswers();
        advancedInfo.setProAnswer1(surveyAnswers.size() > 0 ? surveyAnswers.get(0) : null);
        advancedInfo.setProAnswer2(surveyAnswers.size() > 1 ? surveyAnswers.get(1) : null);
        advancedInfo.setProAnswer3(surveyAnswers.size() > 2 ? surveyAnswers.get(2) : null);
        advancedInfo.setProAnswer4(surveyAnswers.size() > 3 ? surveyAnswers.get(3) : null);
        advancedInfo.setProAnswer5(surveyAnswers.size() > 4 ? surveyAnswers.get(4) : null);

        professionalMapper.updateProfessionalAdvancedInfo(advancedInfo);
    }

    public int getItemNoByPortfolio(int portfolioNo) {
        return professionalMapper.getItemNoByPortfolio(portfolioNo);
    }

}