package com.onestack.project.domain;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Community {
	private Integer communityBoardNo;
    private Integer memberNo;
    private String communityBoardTitle;
    private String communityBoardContent;
    private Timestamp communityBoardRegDate;
    private Integer communityView;
    private String communityFile;
    private Integer communityBoardLike;
    private Integer communityBoardDislike;
    private Integer communityBoardActivation;
    private Integer communityReplyCount;

    private int communityReplyNo;
    private String communityReplyContent;
    private Timestamp communityReplyRegDate;
    private boolean communityReplyActivation;

    private String nickname;
    private String memberStop;
}
