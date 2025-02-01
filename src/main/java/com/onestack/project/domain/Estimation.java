package com.onestack.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estimation {
    private int estimationNo;
    private int memberNo;
    private int proNo;
    private int itemNo;
    private String estimationContent;
    private double estimationPrice;
    private String estimationMsg;
    private int estimationIsread;    // TINYINT 타입과 매칭 (0: 안읽음, 1: 읽음)
    private int progress;            // 0(요청) 1(채팅) 2(결제) 3(완료) 4(거절)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int estimationCheck;     // TINYINT 타입과 매칭 (0: 확인 전, 1: 확인 후)
    
    // 조인 결과를 담을 필드들
    private String memberNickname;
    private String proNickname;
    private String categoryName;
}
