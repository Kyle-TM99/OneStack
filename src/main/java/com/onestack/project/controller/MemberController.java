package com.onestack.project.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onestack.project.domain.Member;
import com.onestack.project.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/updateMember")
    public String updateMember(HttpSession session, Member member, MultipartFile profileImage) {
        Member sessionMember = (Member) session.getAttribute("member");
        member.setMemberNo(sessionMember.getMemberNo());

        // 소셜 로그인 여부 확인 (예: sessionMember.getSocialLogin()이 true인 경우 소셜 로그인)
        boolean isSocialLogin = sessionMember.isSocial(); // 소셜 로그인 여부를 확인하는 메서드 또는 필드

        // 이름을 기존 정보로 유지
        if (isSocialLogin) {
            member.setName(sessionMember.getName()); // 소셜 로그인인 경우 기존 이름 유지
        } else {
            // 이름이 null이거나 비어있지 않은지 확인
            if (member.getName() == null || member.getName().isEmpty()) {
                return "redirect:/updateMemberForm?error=nameRequired"; // 오류 메시지와 함께 리다이렉트
            }
        }

        // 프로필 이미지 처리 로직 추가 (필요한 경우)
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                // 파일 저장 로직 구현
                String fileName = saveProfileImage(profileImage);
                member.setMemberImage(fileName);
            } catch (IOException e) {
                log.error("프로필 이미지 저장 실패", e);
            }
        }

        // 회원 정보 업데이트
        memberService.updateMember(member);
        memberService.updateSocialMember(member);

        // 세션 정보 업데이트
        updateSessionMember(session, member);

        return "redirect:/updateMemberForm"; // 수정 후 같은 페이지로 리다이렉트
    }

    // 세션 정보 업데이트 메서드 분리
    private void updateSessionMember(HttpSession session, Member updatedMember) {
        Member sessionMember = (Member) session.getAttribute("member");
        sessionMember.setName(updatedMember.getName());
        sessionMember.setMemberId(updatedMember.getMemberId());
        sessionMember.setNickname(updatedMember.getNickname());
        sessionMember.setEmail(updatedMember.getEmail());
        sessionMember.setPhone(updatedMember.getPhone());
        sessionMember.setZipcode(updatedMember.getZipcode());
        sessionMember.setAddress(updatedMember.getAddress());
        sessionMember.setAddress2(updatedMember.getAddress2());
        sessionMember.setEmailGet(updatedMember.isEmailGet());

        // 프로필 이미지가 업데이트된 경우
        if (updatedMember.getMemberImage() != null) {
            sessionMember.setMemberImage(updatedMember.getMemberImage());
        }
    }

    // 프로필 이미지 저장 메서드 (필요한 경우)
    private String saveProfileImage(MultipartFile profileImage) throws IOException {
        String originalFilename = profileImage.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;

        // 파일 저장 경로 설정
        Path uploadPath = Paths.get("src/main/resources/static/images/profile");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(uniqueFileName);
        profileImage.transferTo(filePath.toFile());

        return uniqueFileName;
    }

    @GetMapping("/updateMemberForm")
    public String myPage(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");

        // 세션의 member 객체에서 memberNo 직접 사용
        int memberNo = member.getMemberNo();
        model.addAttribute("member", member);

        session.setAttribute("socialType", member.getSocialType());

        return "member/EditMemberUpdateForm";
    }


    @PostMapping("/login")
    public String login(Model model, @RequestParam("memberId") String memberId, @RequestParam("pass") String pass, HttpSession session, HttpServletResponse response)
            throws ServletException, IOException {
        int result = memberService.login(memberId, pass);
        if(result == -1 || result == 0) { // 회원 아이디가 존재하지 않으면
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println(" alert('존재하지 않는 아이디 혹은 비밀번호입니다.');");
            out.println(" location.href='loginForm'");
            out.println("</script>");
            return null;
        }

        Member member = memberService.getMember(memberId);
        session.setAttribute("isLogin", true);
        session.setAttribute("member", member);
        return "redirect:/main";
    }

    @GetMapping("/memberLogout")
    public String logout(HttpSession session) {
        log.info("MemberController.logout(HttpSession session)");
        session.invalidate();
        return "redirect:/loginForm";
    }

    @PostMapping("/join")
    @ResponseBody
    public Map<String, Object> join(@RequestBody Member member) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("받은 회원 데이터: {}", member);

            // 비밀번호 암호화
            member.setPass(passwordEncoder.encode(member.getPass()));

            member.setSocial(false);
            member.setSocialType("none");
            member.setMemberImage("/images/defaultProfile.png");



            // 회원 등록
            int result = memberService.insertMember(member);
            log.info("회원가입 결과: {}", result);

            if (result > 0) {
                response.put("status", "success");
                response.put("message", "회원가입이 완료되었습니다.");
            } else {
                response.put("status", "error");
                response.put("message", "회원가입에 실패했습니다. 다시 시도해주세요.");
            }
        } catch (IllegalArgumentException e) {
            // 유효성 검사나 중복 체크 실패
            log.warn("회원가입 유효성 검사 실패: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", e.getMessage());
        } catch (Exception e) {
            // 기타 예외
            log.error("회원가입 처리 중 오류 발생: ", e);
            response.put("status", "error");
            response.put("message", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }

        return response;

    }


    @PostMapping("/checkId")
    @ResponseBody
    public Map<String, Boolean> checkId(@RequestParam("memberId") String memberId) {
        Map<String, Boolean> response = new HashMap<>();
        int count = memberService.checkMemberId(memberId);
        response.put("available", count == 0);
        return response;
    }

    @PostMapping("/checkNickname")
    @ResponseBody
    public Map<String, Boolean> checkNickname(@RequestParam("nickname") String nickname) {
        Map<String, Boolean> response = new HashMap<>();
        int count = memberService.checkNickname(nickname);
        response.put("available", count == 0);
        return response;
    }

    @PostMapping("/checkPhone")
    @ResponseBody
    public Map<String, Boolean> checkPhone(@RequestParam("phone") String phone) {
        Map<String, Boolean> response = new HashMap<>();
        int count = memberService.checkPhone(phone);
        response.put("available", count == 0);
        return response;
    }

    @PostMapping("/checkEmail")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam("email") String email) {
        Map<String, Boolean> response = new HashMap<>();
        int count = memberService.checkEmail(email);
        response.put("available", count == 0);
        return response;
    }


    @GetMapping("/findId")
    public String findIdForm() {
        return "member/findId";  // findId.html을 보여줌
    }

    @PostMapping("/findId")
    public String findId(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "phone") String phone,
            Model model) {

        try {
            Member member = new Member();
            member.setName(name);
            member.setPhone(phone);

            String memberId = memberService.findMemberId(member);

            if (memberId != null) {
                // 일반 회원 아이디 존재
                model.addAttribute("memberId", memberId);
                model.addAttribute("found", true);
                model.addAttribute("socialType", null);
            } else {
                // 소셜 로그인 회원이거나 일치하는 회원 없음
                String socialType = memberService.findSocialMemberId(member);

                if (socialType != null) {
                    // 소셜 로그인 회원
                    model.addAttribute("found", false);
                    model.addAttribute("socialType", socialType);
                } else {
                    // 완전히 일치하는 회원 없음
                    model.addAttribute("found", false);
                    model.addAttribute("socialType", null);
                }
            }
        } catch (Exception e) {
            model.addAttribute("found", false);
            model.addAttribute("socialType", null);
        }

        return "member/findId";
    }

    @GetMapping("/findPass")
    public String showFindPassForm() {
        return "member/findPass";
    }

    @PostMapping("/findPass")
    @ResponseBody
    public Map<String, Object> findPass(
        @RequestParam(name = "memberId") String memberId, 
        @RequestParam(name = "email") String email
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            memberService.sendPasswordResetEmail(memberId, email);
            response.put("status", "success");
            response.put("message", "비밀번호 재설정 링크가 이메일로 발송되었습니다.");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam(name = "token") String token, Model model) {
        model.addAttribute("token", token);
        return "member/resetPassword";
    }
    @PostMapping("/resetPassword")
    @ResponseBody
    public Map<String, Object> resetPassword(
        @RequestParam(name = "token") String token, 
        @RequestParam(name = "newPassword") String newPassword
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 디버깅을 위한 로그 추가
            log.info("Token: {}, NewPassword: {}", token, newPassword);
            
            memberService.resetPassword(token, newPassword);
            response.put("status", "success");
            response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            // 에러 로그 추가
            log.error("비밀번호 재설정 실패", e);
        }
        return response;
    }
}