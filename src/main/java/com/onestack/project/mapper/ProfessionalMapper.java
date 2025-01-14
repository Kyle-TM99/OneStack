package com.onestack.project.mapper;

import com.onestack.project.domain.Professional;
import com.onestack.project.domain.ProfessionalAdvancedInformation;
import com.onestack.project.domain.Portfolio;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProfessionalMapper {

    // 전문가 기본 정보 저장
    int addPro(Professional professional);

    // 전문가 고급 정보 저장
    int addProAdvancedInfo(ProfessionalAdvancedInformation professionalAdvancedInformation);

    // 포트폴리오 저장
    int addPortfolio(Portfolio portfolio);
}
