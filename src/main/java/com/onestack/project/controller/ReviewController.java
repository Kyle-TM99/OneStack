package com.onestack.project.controller;

import com.onestack.project.service.ChatService;
import com.onestack.project.service.MemberService;
import com.onestack.project.service.ProService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.onestack.project.domain.ChatMessage;
import com.onestack.project.domain.ChatRoom;
import com.onestack.project.domain.Member;
import com.onestack.project.domain.Review;
import com.onestack.project.service.ReviewService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;
    
    @Autowired
    private ProService proService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createReview(@RequestBody Review review) {
        Map<String, Object> response = new HashMap<>();
        try {
            reviewService.createReview(review);
            memberService.updateEstimationProgress(review.getEstimationNo(), 4);

            ChatRoom chatRoom = chatService.findRoom(review.getRoomId());

            Member member = memberService.getMemberByNo(chatRoom.getMemberNo());

            List<Review> reviewList = reviewService.getReviewList(review.getProNo());

            // 평점 계산
            int sum = 0;
            int cnt = 0;
            for(Review r : reviewList) {
                sum += r.getReviewRate();
                cnt++;
            }
            sum /= cnt;

            proService.updateProRating(sum, review.getProNo());

            response.put("success", true);
            response.put("message", "리뷰가 등록되었습니다.");

            // 시스템 메시지 전송
            ChatMessage systemMessage = new ChatMessage();
            systemMessage.setRoomId(review.getRoomId());
            systemMessage.setSender(member.getMemberId());
            systemMessage.setNickname(member.getNickname());
            systemMessage.setType("SYSTEM");
            systemMessage.setMessage("회원이 리뷰를 작성하였습니다.");
            systemMessage.setSentAt(LocalDateTime.now());

            // DB에 시스템 메시지 저장
            chatService.saveMessage(systemMessage);

            // 시스템 메시지를 웹소켓으로 전송
            messagingTemplate.convertAndSend("/topic/chat/room/" + review.getRoomId(), systemMessage);

            // 전문가 리뷰 수 증가
            reviewService.increaseReviewCount(review.getProNo());

            // 견적 상태 수정
            memberService.updateEstimationProgress(review.getEstimationNo(), 4);




        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
} 