package com.onestack.project.ajax;


import com.onestack.project.domain.*;
import com.onestack.project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private final MemberService memberService;


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



    @PostMapping("/changePassword")
    public ResponseEntity<Map<String, Object>> changePassword(HttpSession session,
                                                              @RequestBody Map<String, String> requestBody
    ) {
        Map<String, Object> response = new HashMap<>();

        // 세션에서 memberId 가져오기
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            response.put("success", false);
            response.put("message", "로그인 세션이 만료되었습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Show 접미사가 붙은 파라미터로 변경
        String currentPassword = requestBody.get("currentPasswordShow");
        String newPassword = requestBody.get("newPasswordShow");
        String confirmPassword = requestBody.get("confirmPasswordShow");

        // 새 비밀번호 일치 검증
        if (!newPassword.equals(confirmPassword)) {
            response.put("success", false);
            response.put("message", "새 비밀번호가 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        // 비밀번호 변경 시도
        boolean result = memberService.changePassword(
                member.getMemberId(),
                currentPassword,
                newPassword
        );

        if (result) {
            response.put("success", true);
            response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "현재 비밀번호가 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
}