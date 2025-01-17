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
public class MemberWithInquiry {
	private int inquiryNo;
	private String inquiryTitle;
	private Timestamp inquiryRegDate;
	private boolean inquiryStatus;

	private Member member;
	private Inquiry inquiry;
}
