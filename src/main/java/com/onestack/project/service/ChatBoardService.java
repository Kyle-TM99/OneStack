package com.onestack.project.service;

import com.onestack.project.domain.ChatBoardEvent;
import com.onestack.project.mapper.ChatBoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ChatBoardService {
    
    @Autowired
    private ChatBoardMapper chatBoardMapper;
    
    public void createBoard(ChatBoardEvent board) {
        chatBoardMapper.insertBoard(board);
    }
    
    public List<ChatBoardEvent> getBoardList(String roomId) {
        return chatBoardMapper.getBoardList(roomId);
    }
    
    public ChatBoardEvent getBoard(Long boardId) {
        return chatBoardMapper.getBoard(boardId);
    }
    
    public void updateBoard(ChatBoardEvent board) {
        chatBoardMapper.updateBoard(board);
    }
    
    public void deleteBoard(Long boardId) {
        chatBoardMapper.deleteBoard(boardId);
    }
} 