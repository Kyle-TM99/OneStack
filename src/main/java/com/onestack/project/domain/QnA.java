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
public class QnA {
	private int qnaBoardNo;
    private int memberNo;
    private String qnaBoardTitle;
    private String qnaBoardContent;
    private Timestamp qnaBoardRegDate;
    private int qnaView;
    private String qnaFile;
    private int qnaBoardLike;
    private boolean qnaAdoptionStatus;
    private boolean qnaBoardActivation;
}
