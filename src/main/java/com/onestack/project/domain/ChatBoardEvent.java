package com.onestack.project.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatBoardEvent {
    private Long boardId;
    private String roomId;
    private String memberId;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime joinedAt;

} 