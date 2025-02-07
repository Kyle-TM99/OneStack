package com.onestack.project.controller;

import com.onestack.project.domain.ChatCalendarEvent;
import com.onestack.project.service.ChatCalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat/calendar")
public class ChatCalendarController {
    
    private static final Logger log = LoggerFactory.getLogger(ChatCalendarController.class);
    
    @Autowired
    private ChatCalendarService chatCalendarService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createEvent(@RequestBody ChatCalendarEvent event) {
        Map<String, Object> response = new HashMap<>();
        try {
            chatCalendarService.createEvent(event);
            response.put("success", true);
            response.put("message", "일정이 추가되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{roomId}")
    public ResponseEntity<List<ChatCalendarEvent>> getEventList(@PathVariable String roomId) {
        log.info("일정 조회 요청 - roomId: {}", roomId);
        List<ChatCalendarEvent> events = chatCalendarService.getEventList(roomId);
        log.info("조회된 일정: {}", events);
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/detail/{eventId}")
    public ResponseEntity<ChatCalendarEvent> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(chatCalendarService.getEvent(eventId));
    }
    
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