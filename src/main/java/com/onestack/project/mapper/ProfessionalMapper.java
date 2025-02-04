package com.onestack.project.mapper;

import com.onestack.project.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface ProfessionalMapper {

    // 전문가 기본 정보 저장
    int addPro(Professional professional);

    // 전문가 고급 정보 저장
    int addProAdvancedInfo(ProfessionalAdvancedInformation professionalAdvancedInformation);

    // 포트폴리오 저장
    int addPortfolio(Portfolio portfolio);

    // 전문가 정보 조회
    public List<MemberWithProfessional> getPro2(@Param("proNo") int proNo);

    // 전문가 리뷰 조회
    public List<ProReview> getProReview(@Param("proNo") int proNo);

    // 전문가 심사신청시 멤버타입 업데이트
    void updateMemberType(@Param("memberType") int memberType, @Param("memberNo")int memberNo);

    // 포트폴리오 조회
    List<Portfolio> getPortfoliosByMember(@Param("memberNo") int memberNo);

    // 전문가 여부 확인
    Professional findByMemberNo(@Param("memberNo") int memberNo);
    // 같은 itemNo가 있는지 확인
    Professional findByMemberNoAndItemNo(@Param("memberNo") int memberNo, @Param("itemNo") int itemNo);

    // 같은 itemNo가 있는지 확인(수정하고 있는 포폴제외)
    int countExistingItemNoExcludingCurrent(@Param("proNo") int proNo, @Param("itemNo") int itemNo, @Param("proAdvancedNo") int proAdvancedNo);

    //포트폴리오/전문가고급정보/전문가 삭제
    void deletePortfolio(int portfolioNo);
    void deleteProfessionalAdvancedInfo(int proAdvancedNo);
    void deleteProfessional(int proNo);
    ProfessionalInfo getProfessionalInfoByPortfolio(int portfolioNo);

    // ✅ 포트폴리오 조회 (PortfolioDetail 반환)
    PortfolioDetail getPortfolioById(@Param("portfolioNo") int portfolioNo);

    // ✅ 전문가 정보 조회
    Professional getProfessionalByPortfolio(@Param("portfolioNo") int portfolioNo);

    // ✅ 전문가 고급 정보 조회
    ProfessionalAdvancedInformation getAdvancedInfoByPortfolio(@Param("portfolioNo") int portfolioNo);

    int getItemNoByPortfolio(int portfolioNo);

    MemProPortPaiCate getPortfolioDetail(@Param("portfolioNo") int portfolioNo);

    // ✅ 전문가 정보 업데이트
    Portfolio getPortfolioEntityById(@Param("portfolioNo") int portfolioNo);

    void updatePortfolio(Portfolio portfolio);

    void updateProfessional(Professional professional);

    void updateProfessionalAdvancedInfo(ProfessionalAdvancedInformation advancedInfo);

    Map<String, Object> getPortfolioFiles(@Param("portfolioNo") int portfolioNo);

}
