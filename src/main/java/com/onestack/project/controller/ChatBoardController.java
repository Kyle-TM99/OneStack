package com.onestack.project.controller;

import com.onestack.project.domain.ChatBoardEvent;
import com.onestack.project.domain.ChatMessage;
import com.onestack.project.domain.Member;
import com.onestack.project.service.ChatBoardService;
import com.onestack.project.service.ChatService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat/posts")
public class ChatBoardController {
    
    @Autowired
    private ChatBoardService chatBoardService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    // 게시글 작성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBoard(@RequestBody ChatBoardEvent board, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            chatBoardService.createBoard(board);
            response.put("success", true);
            response.put("message", "게시글이 작성되었습니다.");

            Member member = (Member) session.getAttribute("member");

            // 시스템 메시지 생성
            ChatMessage systemMessage = new ChatMessage();
            systemMessage.setRoomId(board.getRoomId());
            systemMessage.setSender(member.getMemberId());
            systemMessage.setNickname(member.getNickname());
            systemMessage.setType("SYSTEM");
            systemMessage.setMessage(member.getNickname() + "님이 게시글을 작성하였습니다.");
            systemMessage.setSentAt(LocalDateTime.now());

            // DB에 시스템 메시지 저장
            chatService.saveMessage(systemMessage);

            // 시스템 메시지를 웹소켓으로 전송
            messagingTemplate.convertAndSend("/topic/chat/room/" + board.getRoomId(), systemMessage);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    
    // 게시글 목록 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<List<ChatBoardEvent>> getBoardList(@PathVariable String roomId) {
        return ResponseEntity.ok(chatBoardService.getBoardList(roomId));
    }
    
    // 게시글 상세 조회
    @GetMapping("/detail/{boardId}")
    public ResponseEntity<ChatBoardEvent> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(chatBoardService.getBoard(boardId));
    }
    
    // 게시글 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> updateBoard(
            @PathVariable Long boardId,
            @RequestBody ChatBoardEvent board,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            board.setBoardId(boardId);

            chatBoardService.updateBoard(board);
            response.put("success", true);
            response.put("message", "게시글이 수정되었습니다.");

            Member member = (Member) session.getAttribute("member");

            // 시스템 메시지 생성
            ChatMessage systemMessage = new ChatMessage();
            systemMessage.setRoomId(board.getRoomId());
            systemMessage.setSender(member.getMemberId());
            systemMessage.setNickname(member.getNickname());
            systemMessage.setType("SYSTEM");
            systemMessage.setMessage(member.getNickname() + "님이 게시글을 수정하였습니다.");
            systemMessage.setSentAt(LocalDateTime.now());

            // DB에 시스템 메시지 저장
            chatService.saveMessage(systemMessage);

            // 시스템 메시지를 웹소켓으로 전송
            messagingTemplate.convertAndSend("/topic/chat/room/" + board.getRoomId(), systemMessage);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    // 게시글 삭제  
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> deleteBoard(@PathVariable Long boardId) {
        Map<String, Object> response = new HashMap<>();
        try {
            chatBoardService.deleteBoard(boardId);
            response.put("success", true);
            response.put("message", "게시글이 삭제되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
} 