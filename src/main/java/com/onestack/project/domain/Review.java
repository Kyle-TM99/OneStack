package com.onestack.project.domain;

import java.util.Date;

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
    // 리뷰 평점
    private int reviewRate;
    private Date reviewDate;
    private byte reviewActivation;

    // 견적 번호
    private int estimationNo;

    // 채팅방 번호
    private String roomId;
    
    // 회원 닉네임 필드 추가
    private String memberNickname;
    private String proNickname;
}
