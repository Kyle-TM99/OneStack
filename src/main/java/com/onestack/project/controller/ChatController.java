package com.onestack.project.controller;

import com.onestack.project.domain.ChatMessage;
import com.onestack.project.domain.ChatRoom;
import com.onestack.project.domain.Estimation;
import com.onestack.project.domain.Member;
import com.onestack.project.service.ChatService;
import com.onestack.project.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MemberService memberService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 채팅방 목록 페이지
    @GetMapping("/chat")
    public String chatList(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/loginForm";
        }
        
        try {
            // 사용자가 참여중인 채팅방 목록 조회
            List<ChatRoom> chatRooms = chatService.getMyChatRooms(member.getMemberNo());
            model.addAttribute("chatRooms", chatRooms);
            return "views/chat_list";
        } catch (Exception e) {
            log.error("채팅방 목록 조회 실패: {}", e.getMessage());
            return "redirect:/";
        }
    }

    // 채팅방 입장
    @GetMapping("/chat/room/{roomId}")
    public String enterRoom(@PathVariable String roomId, Model model, HttpSession session) {
        try {
            Member member = (Member) session.getAttribute("member");
            if (member == null) {
                return "redirect:/loginForm";
            }
            
            // 채팅방 정보 조회
            ChatRoom room = chatService.findRoom(roomId);
            if (room == null) {
                return "redirect:/chat?error=not_found";
            }
            
            // 참여 권한 확인
            if (!chatService.isParticipant(roomId, member.getMemberNo())) {
                return "redirect:/chat?error=unauthorized";
            }

            // 견적 정보 조회
            Estimation estimation = memberService.getEstimationByNo(room.getEstimationNo());
            
            // 채팅방 정보 모델에 추가
            model.addAttribute("room", room);
            model.addAttribute("member", member);
            model.addAttribute("estimation", estimation);
            
            // 최근 메시지 조회 (최근 50개)
            List<ChatMessage> messages = chatService.getRecentMessages(roomId, 50);
            model.addAttribute("messages", messages);
            
            return "views/chat";
        } catch (Exception e) {
            log.error("채팅방 입장 실패: {}", e.getMessage());
            // 한글 에러 메시지 대신 에러 코드 사용
            return "redirect:/chat?error=server_error";
        }
    }

    // 메시지 전송
    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatMessage message) {
        try {
            log.info("Received message: {}", message);
            
            // 메시지 저장
            message.setSentAt(LocalDateTime.now());
            chatService.saveMessage(message);
            
            // 특정 채팅방으로 메시지 전송
            messagingTemplate.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
            
        } catch (Exception e) {
            log.error("메시지 전송 실패: {}", e.getMessage());
            throw new RuntimeException("메시지 전송에 실패했습니다.");
        }
    }

    // 채팅방 삭제
    @PostMapping("/api/chat/room/{roomId}/delete")
    @ResponseBody
    public Map<String, Object> deleteRoom(@PathVariable String roomId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 시스템 메시지 전송
            ChatMessage systemMessage = new ChatMessage();
            systemMessage.setRoomId(roomId);
            systemMessage.setType("SYSTEM");
            systemMessage.setMessage("채팅방이 삭제되었습니다.");
            systemMessage.setSentAt(LocalDateTime.now());
            
            // 시스템 메시지를 웹소켓으로 전송
            messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, systemMessage);
            
            // 잠시 대기 후 채팅방 삭제 (메시지가 전송될 시간을 주기 위해)
            Thread.sleep(500);
            
            chatService.deleteChatRoom(roomId);
            response.put("success", true);
            response.put("message", "채팅방이 삭제되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
} 