package com.onestack.project.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryAnswer {
	private int inquiryAnswerNo;
	private int inquiryNo;
	private int managerNo;
	private String inquiryAnswerContent;
	private Timestamp inquiryAnswerRegDate;
	private String inquiryAnswerFile;	
}

