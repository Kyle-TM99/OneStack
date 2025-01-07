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
public class Notice {
	private int noticeNo;
	private int managerNo;
	private String noticeContent;
	private Timestamp noticeRegDate;
	private int noticeView;
	private String noticeFile;
}
