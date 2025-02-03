package com.onestack.project.mapper;

import com.onestack.project.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    void updateMemberType(@Param("memberType") int memberType, @Param("memberNo")int memberNo);

}
