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
public class Review {
	private int reviewNo;
    private int proNo;
    private int memberNo;
    private String reviewContent;
    private int reviewRate;
    private Timestamp reviewDate;
    private byte reviewActivation;
}
