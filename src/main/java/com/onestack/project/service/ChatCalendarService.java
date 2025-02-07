package com.onestack.project.service;

import com.onestack.project.domain.ChatCalendarEvent;
import com.onestack.project.mapper.ChatCalendarMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ChatCalendarService {
    
    @Autowired
    private ChatCalendarMapper chatCalendarMapper;
    
    public void createEvent(ChatCalendarEvent event) {
        chatCalendarMapper.insertEvent(event);
    }
    
    public List<ChatCalendarEvent> getEventList(String roomId) {
        try {
            List<ChatCalendarEvent> events = chatCalendarMapper.getEventList(roomId);
            log.info("조회된 일정 목록: {}", events);
            return events;
        } catch (Exception e) {
            log.error("일정 목록 조회 중 오류: {}", e.getMessage(), e);
            throw new RuntimeException("일정 목록을 조회하는 중 오류가 발생했습니다.", e);
        }
    }
    
    public ChatCalendarEvent getEvent(Long eventId) {
        return chatCalendarMapper.getEvent(eventId);
    }
    
    public void updateEvent(ChatCalendarEvent event) {
        chatCalendarMapper.updateEvent(event);
    }
    
    public void deleteEvent(Long eventId) {
        chatCalendarMapper.deleteEvent(eventId);
    }
} 