package com.onestack.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessage {
    private Long messageId;      // 메시지 ID
    private String roomId;       // 채팅방 ID
    private String sender;       // 보내는 사람
    private String nickname;     // 닉네임 필드 추가
    private String message;      // 메시지
    private String type;     // 메시지 타입 (ENTER, TALK, LEAVE)
    private LocalDateTime sentAt;  // 전송 시간
    private String socialType;    // 추가
    private String profileImage;  // 추가
} 