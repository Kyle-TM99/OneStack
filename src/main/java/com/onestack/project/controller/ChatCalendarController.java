package com.onestack.project.controller;

import com.onestack.project.domain.ChatCalendarEvent;
import com.onestack.project.domain.ChatMessage;
import com.onestack.project.domain.Member;
import com.onestack.project.service.ChatCalendarService;
import com.onestack.project.service.ChatService;

import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat/calendar")
public class ChatCalendarController {
    
    private static final Logger log = LoggerFactory.getLogger(ChatCalendarController.class);
    
    @Autowired
    private ChatCalendarService chatCalendarService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    //일정 등록
    @PostMapping

    public ResponseEntity<Map<String, Object>> createEvent(@RequestBody ChatCalendarEvent event, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            chatCalendarService.createEvent(event);
            response.put("success", true);
            response.put("message", "일정이 추가되었습니다.");

            Member member = (Member) session.getAttribute("member");

            // 시스템 메시지 생성
            ChatMessage systemMessage = new ChatMessage();
            systemMessage.setRoomId(event.getRoomId());
            systemMessage.setSender(member.getMemberId());
            systemMessage.setNickname(member.getNickname());
            systemMessage.setType("SYSTEM");
            systemMessage.setMessage(member.getNickname() + "님이 일정을 등록하였습니다.");
            systemMessage.setSentAt(LocalDateTime.now());

            // DB에 시스템 메시지 저장
            chatService.saveMessage(systemMessage);

            // 시스템 메시지를 웹소켓으로 전송
            messagingTemplate.convertAndSend("/topic/chat/room/" + event.getRoomId(), systemMessage);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    //일정 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<List<ChatCalendarEvent>> getEventList(@PathVariable String roomId) {
        try {
            log.info("일정 조회 요청 - roomId: {}", roomId);
            if (roomId == null || roomId.trim().isEmpty()) {
                throw new IllegalArgumentException("방 ID가 유효하지 않습니다.");
            }
            
            List<ChatCalendarEvent> events = chatCalendarService.getEventList(roomId);
            log.info("조회된 일정 수: {}", events.size());
            log.info("조회된 일정 데이터: {}", events);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            log.error("일정 조회 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }
    
    //일정 상세 조회
    @GetMapping("/detail/{eventId}")
    public ResponseEntity<ChatCalendarEvent> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(chatCalendarService.getEvent(eventId));
    }
    
    //일정 수정
    @PutMapping("/{eventId}")
    public ResponseEntity<Map<String, Object>> updateEvent(
            @PathVariable Long eventId,
            @RequestBody ChatCalendarEvent event) {
        Map<String, Object> response = new HashMap<>();
        try {
            event.setEventId(eventId);
            chatCalendarService.updateEvent(event);
            response.put("success", true);
            response.put("message", "일정이 수정되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    
    //일정 삭제
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Map<String, Object>> deleteEvent(@PathVariable Long eventId) {
        Map<String, Object> response = new HashMap<>();
        try {
            chatCalendarService.deleteEvent(eventId);
            response.put("success", true);
            response.put("message", "일정이 삭제되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
} 