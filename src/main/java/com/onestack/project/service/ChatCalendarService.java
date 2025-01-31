package com.onestack.project.service;

import com.kyletalk.sns.domain.ChatCalendarEvent;
import com.kyletalk.sns.mapper.ChatCalendarMapper;
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
        return chatCalendarMapper.getEventList(roomId);
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