package com.onestack.project.controller;

import com.onestack.project.domain.ChatMessage;
import com.onestack.project.domain.ChatRoom;
import com.onestack.project.domain.Estimation;
import com.onestack.project.domain.Member;
import com.onestack.project.service.MemberService;
import com.onestack.project.service.ChatService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EstimationController {
    
    private final MemberService memberService;
    private final ChatService chatService;  // ChatService 주입
    private final SimpMessagingTemplate messagingTemplate;
    

    // 견적 페이지
    @GetMapping("/estimation")
    public String showEstimationPage(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            Model model, 
            HttpSession session
    ) {
        Member member = (Member) session.getAttribute("member");
        
        if (member == null || member.getMemberType() != 1) {
            return "redirect:/";
        }
        
        int pageSize = 10;  // 한 페이지당 보여줄 견적 수
        int pageGroup = 5;  // 페이지 그룹 크기
        
        // 전체 견적 수와 페이지 계산
        int totalCount = memberService.getEstimationCount(member.getMemberNo());
        int pageCount = (int) Math.ceil((double) totalCount / pageSize);
        
        int startPage = (pageNum - 1) / pageGroup * pageGroup + 1;
        int endPage = Math.min(startPage + pageGroup - 1, pageCount);
        
        // 현재 페이지의 견적 목록 조회
        List<Estimation> estimations = memberService.getEstimationsByPage(member.getMemberNo(), pageNum, pageSize);
        
        // 페이지네이션 정보를 모델에 추가
        model.addAttribute("estimations", estimations);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("pageGroup", pageGroup);
        
        return "views/estimation";
    }

    // 견적 거절
    @PostMapping("/api/estimation/{estimationNo}/delete")
    @ResponseBody
    public Map<String, Object> deleteEstimation(
            @PathVariable int estimationNo,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            Member member = (Member) session.getAttribute("member");
            // progress를 5(거절)로 업데이트
            memberService.updateEstimationProgress(estimationNo, 5);
            
            // 현재 페이지의 새로운 견적 목록 조회
            List<Estimation> newEstimations = memberService.proEstimation(member.getMemberNo());
            
            response.put("success", true);
            response.put("message", "견적이 거절되었습니다.");
            response.put("estimations", newEstimations);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "처리 중 오류가 발생했습니다.");
        }
        return response;
    }

    // 견적 매칭 및 채팅방 생성
    @PostMapping("/api/estimation/{estimationNo}/match")
    @ResponseBody
    public Map<String, Object> matchEstimation(
            @PathVariable int estimationNo,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            Member member = (Member) session.getAttribute("member");
            
            // 1. 견적 상태를 매칭(1)으로 업데이트
            memberService.updateEstimationProgress(estimationNo, 1);
            
            // 2. 견적 정보 조회
            Estimation estimation = memberService.getEstimationByNo(estimationNo);
            
            // 3. 채팅방 생성
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setRoomName(estimation.getMemberNickname() + " 님이 " + member.getNickname() + " 님에게 요청한 " + estimation.getCategoryName() + " 프로젝트");
            chatRoom.setMemberNo(estimation.getMemberNo());
            chatRoom.setProNo(member.getMemberNo());
            chatRoom.setEstimationNo(estimationNo);
            
            String roomId = chatService.createRoom(chatRoom);
            
            // 4. 새로운 견적 목록 조회 (progress=0인 것만)
            List<Estimation> newEstimations = memberService.proEstimation(member.getMemberNo());
            
            response.put("success", true);
            response.put("message", "매칭이 완료되었습니다.");
            response.put("estimations", newEstimations);
            response.put("roomId", roomId);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "처리 중 오류가 발생했습니다.");
        }
        return response;
    }

    // 견적 확인 처리
    @PostMapping("/api/estimation/{estimationNo}/confirm")
    @ResponseBody
    public Map<String, Object> confirmEstimation(
        @PathVariable int estimationNo,
        @RequestBody Map<String, String> request
    ) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String confirmType = request.get("type");
            String roomId = request.get("roomId");
            ChatRoom chatRoom = chatService.findRoom(roomId);

            Member member = memberService.getMemberByNo(chatRoom.getMemberNo());
            Member pro = memberService.getMemberByNo(chatRoom.getProNo());
            
            if ("PRO".equals(confirmType)) {
                // 전문가 확인 처리
                memberService.confirmEstimationByPro(estimationNo);

                // 시스템 메시지 생성
                ChatMessage systemMessage = new ChatMessage();
                systemMessage.setRoomId(roomId);
                systemMessage.setSender(pro.getMemberId());
                systemMessage.setNickname(pro.getNickname());
                systemMessage.setType("SYSTEM");
                systemMessage.setMessage("전문가 " + pro.getNickname() + " 님이 견적 요청을 수락하였습니다. " + member.getNickname() + " 님의 견적 확인 후 결제를 진행하실 수 있습니다.");
                systemMessage.setSentAt(LocalDateTime.now());

                // DB에 시스템 메시지 저장
                chatService.saveMessage(systemMessage);

                // 시스템 메시지를 웹소켓으로 전송
                messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, systemMessage);
                
                response.put("success", true);
            } else if ("CLIENT".equals(confirmType)) {
                // 의뢰인 확인 처리
                memberService.confirmEstimationByClient(estimationNo);

                // 시스템 메시지 생성
                ChatMessage systemMessage = new ChatMessage();
                systemMessage.setRoomId(roomId);
                systemMessage.setSender(member.getMemberId());
                systemMessage.setNickname(member.getNickname());
                systemMessage.setType("SYSTEM");
                systemMessage.setMessage(member.getNickname() + "님이 최종 견적을 확인하였습니다. 프로젝트 결제를 진행해주세요.");
                systemMessage.setSentAt(LocalDateTime.now());

                // DB에 시스템 메시지 저장
                chatService.saveMessage(systemMessage);

                // 시스템 메시지를 웹소켓으로 전송
                messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, systemMessage);
                
                response.put("success", true);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        
        return response;
    }

    // 견적 금액 수정
    @PostMapping("/api/estimation/{estimationNo}/price")
    @ResponseBody
    public Map<String, Object> updateEstimationPrice(
        @PathVariable int estimationNo,
        @RequestBody Map<String, Integer> request,
        HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Member member = (Member) session.getAttribute("member");
            if (member == null) {
                throw new RuntimeException("로그인이 필요합니다.");
            }

            // 견적 정보 조회
            Estimation estimation = memberService.getEstimationByNo(estimationNo);
            if (estimation == null) {
                throw new RuntimeException("견적을 찾을 수 없습니다.");
            }

            // 권한 체크 (전문가만 수정 가능)
            if (member.getMemberNo() != estimation.getProNo()) {
                throw new RuntimeException("견적 금액 수정 권한이 없습니다.");
            }

            // 금액 업데이트
            int newPrice = request.get("estimationPrice");
            memberService.updateEstimationPrice(estimationNo, newPrice);
            
            response.put("success", true);
            response.put("message", "견적 금액이 수정되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        
        return response;
    }
} 