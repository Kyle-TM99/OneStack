package com.onestack.project.service;

import com.onestack.project.domain.ChatMessage;
import com.onestack.project.domain.ChatRoom;
import com.onestack.project.domain.Member;
import com.onestack.project.mapper.ChatMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ChatService {
    
    @Autowired
    private ChatMapper chatMapper;

    // 채팅방 삭제
    public void deleteChatRoom(String roomId) {
        try {
            // 1. 채팅방의 모든 메시지 삭제
            chatMapper.deleteAllMessages(roomId);
            // 2. 채팅방 삭제
            chatMapper.deleteChatRoom(roomId);
        } catch (Exception e) {
            log.error("채팅방 삭제 실패: {}", e.getMessage());
            throw new RuntimeException("채팅방 삭제에 실패했습니다.", e);
        }
    }
    
    // 채팅방 목록 조회
    // public List<ChatRoom> getAllChatRooms() {
    //    return chatMapper.getAllChatRooms();
    //}

    // 채팅방 생성
    public String createRoom(ChatRoom room) {
        try {
            room.setRoomId(UUID.randomUUID().toString());
            room.setMaxUsers(2);  // 1:1 채팅
            room.setCreatedAt(LocalDateTime.now());
            
            chatMapper.createChatRoom(room);
            return room.getRoomId();
        } catch (Exception e) {
            log.error("채팅방 생성 실패: {}", e.getMessage());
            throw new RuntimeException("채팅방 생성에 실패했습니다.", e);
        }
    }

    // 채팅방 비밀번호 확인
    public void verifyAndJoinRoom(String roomId, String password, Member member) {
        String storedPassword = chatMapper.getRoomPassword(roomId);
        if (storedPassword == null || !storedPassword.equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        
        chatMapper.addParticipant(roomId, member.getMemberId());
        int currentCount = chatMapper.getParticipantCount(roomId);
        chatMapper.updateParticipantCount(roomId, currentCount);
    }

    // 채팅방 참여자 조회
    public List<Member> getRoomParticipants(String roomId) {
        return chatMapper.getRoomParticipants(roomId);
    }

    // 채팅방 나가기
    public void leaveRoom(String roomId, Member member) {
        // 참여자인지 확인
        boolean isParticipant = chatMapper.isParticipant(roomId, member.getMemberId());
        if (!isParticipant) {
            throw new RuntimeException("해당 채팅방의 참여자가 아닙니다.");
        }

        // 방장인지 확인
        boolean isAdmin = chatMapper.isRoomAdmin(roomId, member.getMemberId());
        if (isAdmin) {
            throw new RuntimeException("방장은 방을 나갈 수 없습니다. 먼저 방장을 위임해주세요.");
        }
        
        // 참여자 제거
        chatMapper.removeParticipant(roomId, member.getMemberId());

        // 참여자 수 업데이트
        int currentCount = chatMapper.getParticipantCount(roomId);
        
        // 참여자 수가 0이면 채팅방 삭제
        if (currentCount == 0) {
            chatMapper.deleteChatRoom(roomId);
        } else {
            // 참여자 수 업데이트
            chatMapper.updateParticipantCount(roomId, currentCount);
        }
    }

    // 채팅방 조회
    public ChatRoom findRoom(String roomId) {
        try {
            ChatRoom room = chatMapper.getChatRoomById(roomId);
            if (room == null) {
                throw new RuntimeException("존재하지 않는 채팅방입니다.");
            }
            
            // 최근 메시지 목록 조회 (최근 50개)
            List<ChatMessage> recentMessages = chatMapper.getRecentMessages(roomId, 50);
            room.setRecentMessages(recentMessages);
            
            return room;
        } catch (Exception e) {
            log.error("채팅방 조회 실패: {}", e.getMessage());
            throw new RuntimeException("채팅방 조회에 실패했습니다.", e);
        }
    }

    // 채팅 메시지 저장
    public void saveMessage(ChatMessage message) {
        try {
            log.info("Saving message: {}", message);
            chatMapper.insertMessage(message);
        } catch (Exception e) {
            log.error("메시지 저장 실패: {}", e.getMessage());
            throw new RuntimeException("메시지 저장에 실패했습니다.", e);
        }
    }

    // 채팅방 참여자 확인
    public boolean isParticipant(String roomId, int memberNo) {
        try {
            ChatRoom room = chatMapper.getChatRoomById(roomId);
            return room != null && (room.getMemberNo() == memberNo || room.getProNo() == memberNo);
        } catch (Exception e) {
            log.error("참여자 확인 실패: {}", e.getMessage());
            throw new RuntimeException("참여자 확인에 실패했습니다.", e);
        }
    }

    // 채팅방의 최근 메시지 조회
    public List<ChatMessage> getRecentMessages(String roomId, int limit) {
        try {
            return chatMapper.getRecentMessages(roomId, limit);
        } catch (Exception e) {
            log.error("최근 메시지 조회 실패: {}", e.getMessage());
            throw new RuntimeException("최근 메시지 조회에 실패했습니다.", e);
        }
    }

    // 사용자가 참여중인 채팅방 목록 조회
    public List<ChatRoom> getMyChatRooms(int memberNo) {
        try {
            return chatMapper.getMyChatRooms(memberNo);
        } catch (Exception e) {
            log.error("채팅방 목록 조회 실패: {}", e.getMessage());
            throw new RuntimeException("채팅방 목록 조회에 실패했습니다.", e);
        }
    }
} 