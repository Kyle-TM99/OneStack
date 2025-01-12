package com.onestack.project.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.onestack.project.domain.Portfolio;

@Mapper
public interface ProfessionalMapper {
	void addPortfolio(Portfolio portfolio);
}
