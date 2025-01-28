package com.onestack.project.ajax;


import com.onestack.project.domain.*;
import com.onestack.project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ajax/member")
@RequiredArgsConstructor
public class AjaxMemberController {

    private final MemberService memberService;

    @GetMapping("/myPagePortfolio")
    @ResponseBody
    public Map<String, Object> getMyPagePortfolio(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        // 세션에서 현재 로그인한 회원 정보 가져오기
        Member member = (Member) session.getAttribute("member");

        try {
            // 회원 번호로 리뷰 조회
           // List<MemProWithPortPortImage> reviews = memberService.memProWithPortPortImage(member.getMemberNo());

            response.put("success", true);
            //response.put("data", reviews);
        } catch (Exception e) {
            log.error("리뷰 조회 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
        }

        return response;
    }


    @GetMapping("/myPageActivity")
    @ResponseBody
    public Map<String, Object> getMyPageActivity(HttpSession session,
                                                 @RequestParam(name = "type", defaultValue = "post") String activityType) {
        Map<String, Object> response = new HashMap<>();
        try {
            Member member = (Member) session.getAttribute("member");

            List<Object> data = new ArrayList<>();
            switch (activityType) {
                case "post":
                    List<?> communityData = memberService.memberMyPageCommunity(member.getMemberNo());
                    data.addAll(communityData != null ? communityData : new ArrayList<>());
                    break;

                case "question":
                    List<?> qnaData = memberService.memberMyPageQnA(member.getMemberNo());
                    data.addAll(qnaData != null ? qnaData : new ArrayList<>());
                    break;

                case "postComment":
                    List<?> commentData = memberService.comWithComReply(member.getMemberNo());
                    data.addAll(commentData != null ? commentData : new ArrayList<>());
                    break;

                case "questionComment":
                    List<?> replyData = memberService.qnaWithReply(member.getMemberNo());
                    data.addAll(replyData != null ? replyData : new ArrayList<>());
                    break;

                case "likes":

                    List<?> communityLikes = memberService.memberMyPageCommunityLike(member.getMemberNo());
                    List<?> qnaLikes = memberService.memberMyPageQnALike(member.getMemberNo());
                    List<?> comReplyLikes = memberService.memberMyPageComReplyLike(member.getMemberNo());
                    List<?> qnaReplyLikes = memberService.memberMyPageQnAReplyLike(member.getMemberNo());

                    data.addAll(communityLikes != null ? communityLikes : new ArrayList<>());
                    data.addAll(qnaLikes != null ? qnaLikes : new ArrayList<>());
                    data.addAll(comReplyLikes != null ? comReplyLikes : new ArrayList<>());
                    data.addAll(qnaReplyLikes != null ? qnaReplyLikes : new ArrayList<>());

                    break;

                default:
                    response.put("success", false);
                    response.put("message", "잘못된 활동 유형입니다.");
                    return response;
            }

            response.put("success", true);
            response.put("data", data);

        } catch (Exception e) {
            log.error("Activity 조회 중 오류 발생", e);
            log.error("오류 상세 정보: ", e);
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/portfolio")
    @ResponseBody
    public Map<String, Object> portfolio(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Member member = (Member) session.getAttribute("member");
        Professional professional = (Professional) session.getAttribute("professional");

        try {
            if (member == null) {
                log.warn("Member is null in session.");
                response.put("success", false);
                response.put("message", "회원 정보가 세션에 없습니다.");
                return response; // 조기 반환
            }

            if (professional != null) {
                int proNo = professional.getProNo(); // proNo 가져오기
                List<Portfolio> portfolio = memberService.portfolio(proNo);
                response.put("portfolio", portfolio);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
        }

        return response;
    }


    @GetMapping("/myPageReview")
    @ResponseBody
    public Map<String, Object> getMyPageReview(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        // 세션에서 현재 로그인한 회원 정보 가져오기
        Member member = (Member) session.getAttribute("member");
        Professional professional = (Professional) session.getAttribute("pro");

        try {
            // 회원 번호로 리뷰 조회
            List<Review> reviews = memberService.findMyReview(member.getMemberNo());
            // 전문가 번호로 리뷰 조회
            //List<Review> proReview = memberService.proReview(professional.getProNo());

            response.put("success", true);
            response.put("data", reviews);
        } catch (Exception e) {
            log.error("리뷰 조회 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
        }

        return response;
    }

    @PostMapping("/checkPassword")
    public Map<String, Boolean> checkPassword(@RequestParam("pass") String pass, HttpSession session) {
        Map<String, Boolean> response = new HashMap<>();
        Member member = (Member) session.getAttribute("member");

        if (member != null) {
            boolean isValid = memberService.memberPassCheck(member.getMemberId(), pass);
            response.put("valid", isValid);
        } else {
            response.put("valid", false);
        }

        return response;
    }




    @GetMapping("/getMemberRequest")
    @ResponseBody
    public Map<String, Object> getMemberRequest(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Member member = (Member) session.getAttribute("member");
        Professional professional = (Professional) session.getAttribute("professional");

        try {
            // member가 null인지 확인
            if (member == null) {
                log.warn("Member is null in session.");
                response.put("success", false);
                response.put("message", "회원 정보가 세션에 없습니다.");
                return response; // 조기 반환
            }

            if (professional != null) {
                List<Estimation> proEstimations = memberService.proEstimation(professional.getProNo());
                response.put("proEstimations", proEstimations);
            }

            List<Estimation> memberEstimations = memberService.memberEstimation(member.getMemberNo());
            response.put("memberEstimations", memberEstimations);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
        }

        return response;
    }

    @GetMapping("/getMemberInfo")
    public Member getMemberInfo(HttpSession session) {
        return (Member) session.getAttribute("member");
    }

    @GetMapping("/getPaymentInfo")
    @ResponseBody
    public Map<String, Object> getPaymentInfo(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Member member = (Member) session.getAttribute("member");
        try {
            // 필요한 결제 정보를 가져오는 서비스 메서드 호출
            List<MemberWithProfessional> proMem = memberService.memberWithProfessional(member.getMemberNo());
            response.put("success", true);
            response.put("data", proMem);
        } catch (Exception e) {
            log.error("결제 정보 조회 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
        }

        return response;
    }

    @PostMapping("/updatePassword")
    public Map<String, Object> updatePasswordMember(
            @RequestParam("pass") String pass,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();
        Member member = (Member) session.getAttribute("member");

        if (member != null) {
            // 현재 비밀번호 확인
            boolean isValidPassword = memberService.memberPassCheck(member.getMemberId(), currentPassword);

            if (!isValidPassword) {
                response.put("success", false);
                response.put("message", "현재 비밀번호가 일치하지 않습니다.");
                return response;
            }

            // 새 비밀번호로 업데이트
            member.setPass(newPassword);  // 서비스에서 암호화 처리
            int result = memberService.updatePasswordMember(member);

            if (result > 0) {
                response.put("success", true);
                response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "비밀번호 변경에 실패했습니다.");
            }
        } else {
            response.put("valid", false);
        }

        return response;
    }

}