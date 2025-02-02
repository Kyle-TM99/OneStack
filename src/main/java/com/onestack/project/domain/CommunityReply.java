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
public class CommunityReply {
	private int communityReplyNo;
    private int communityBoardNo;
    private int memberNo;
    private String communityReplyContent;
    private Timestamp communityReplyRegDate;
    private int communityReplyLike;
    private int communityReplyDislike;
    private boolean communityReplyActivation;
    private String nickname;
}
