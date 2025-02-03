package com.onestack.project.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ChatRoom {
    private String roomId;
    private String roomName;
    private int memberNo;    // 일반 회원 ID
    private int proNo;       // 전문가 ID
    private int estimationNo;   // 견적 번호
    private int maxUsers;
    private LocalDateTime createdAt;
    private List<ChatMessage> recentMessages;
    private int roomAdmin;   // 방장 ID (전문가 ID로 설정)
    
    // 견적 관련 필드 추가
    private String estimationContent;  // 견적 내용
    private String memberNickname;     // 일반 회원 닉네임
    private String proNickname;        // 전문가 닉네임
    
    private int progress;  // 채팅방 진행 상태 (0: 대기중, 2: 진행중)
    
    private String lastMessage;  // 마지막 메시지 내용
    private LocalDateTime updatedAt;  // 마지막 업데이트 시간
    private int unreadCount;     // 읽지 않은 메시지 수
    
    @Override
    public String toString() {
        return "ChatRoom{" +
                "roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", memberNo=" + memberNo +
                ", proNo=" + proNo +
                ", estimationNo=" + estimationNo +
                ", maxUsers=" + maxUsers +
                ", createdAt=" + createdAt +
                ", roomAdmin=" + roomAdmin +
                ", estimationContent='" + estimationContent + '\'' +
                ", memberNickname='" + memberNickname + '\'' +
                ", proNickname='" + proNickname + '\'' +
                '}';
    }

    public void setRecentMessages(List<ChatMessage> recentMessages) {
        this.recentMessages = recentMessages;
    }

    public List<ChatMessage> getRecentMessages() {
        return recentMessages;
    }
} 