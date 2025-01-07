package com.onestack.project.domain;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
	private int portfolioNo;
	private int proNo;
	private int proAdvancedNo;
	private String portfolioTitle;
	private String portfolioContent;
	private boolean visibility;

}
