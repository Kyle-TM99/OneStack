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
public class QnAReply {
	private int qnaReplyNo;
    private int qnaBoardNo;
    private int memberNo;
    private String qnaReplyContent;
    private Timestamp qnaReplyRegDate;
    private boolean qnaAdoption;
    private int qnaReplyLike;
    private boolean qnaReplyActivation;
}
