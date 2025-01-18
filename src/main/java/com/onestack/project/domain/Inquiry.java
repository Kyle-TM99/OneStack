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
public class Inquiry {
	private int inquiryNo;
	private int memberNo; 
	private String inquiryTitle;
	private String inquiryContent;
	private Timestamp inquiryRegDate;
	private String inquiryFile;
	private String inquiryStatus;
	private boolean inquirySatisfaction;
}
