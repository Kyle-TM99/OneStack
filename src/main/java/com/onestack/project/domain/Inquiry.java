package com.onestack.project.domain;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

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
	private String inquiryFile;
<<<<<<< HEAD
	private Timestamp inquiryRegDate;
	private boolean inquiryStatus;
=======
	private String inquiryStatus;
>>>>>>> refs/heads/develop
	private boolean inquirySatisfaction;
}
