package com.onestack.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Survey {
	private int surveyNo; 
	private int itemNo;
	private String surveyQuestion; 
	private String surveyOption;
}
