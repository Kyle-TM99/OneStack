package com.onestack.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onestack.project.domain.Portfolio;
import com.onestack.project.mapper.ProfessionalMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfessionalService {

	@Autowired
	private ProfessionalMapper professionalMapper;

	public boolean addPortfolio(Portfolio portfolio) {
		try {
			professionalMapper.addPortfolio(portfolio);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
