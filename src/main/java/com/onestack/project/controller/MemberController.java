package com.onestack.project.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.onestack.project.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    ImageService imageService;

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/ajax/member/changePassword")
    @ResponseBody
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

    @PostMapping("/updateMember")
    @ResponseBody
    public Map<String, Object> updateMember(HttpSession session, Member member,
                               @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        Map<String, Object> response = new HashMap<>();
        try {
            Member sessionMember = (Member) session.getAttribute("member");
            member.setMemberNo(sessionMember.getMemberNo());
            // 소셜 타입 설정 추가
            member.setSocialType(sessionMember.getSocialType());

            if (profileImage != null && !profileImage.isEmpty()) {
                try {
                    // 개발 환경에서 임시로 이미지 URL 생성
                    String fileName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();
                    String imageUrl = "http://3.37.88.97/images/" + fileName;

                    // 실제 파일은 저장하지 않고 URL만 설정 (개발 환경)
                    member.setMemberImage(imageUrl);
                    log.info("개발 환경 - 이미지 URL 설정: {}", imageUrl);

                } catch (Exception e) {
                    log.error("이미지 처리 중 에러 발생: {}", e.getMessage());
                }
            } else {
                member.setMemberImage(sessionMember.getMemberImage());
            }

            // 회원 정보 업데이트
            String socialType = sessionMember.getSocialType();
            if (socialType.equals("kakao") || socialType.equals("google")) {
                member.setMemberId(sessionMember.getMemberId());
                member.setPass(sessionMember.getPass());
                memberService.updateSocialMember(member);
            } else {
                memberService.updateMember(member);
            }

            // DB에서 업데이트된 정보 확인
            Member updatedMember = memberService.getMember(member.getMemberId());
            session.setAttribute("member", updatedMember);
            log.info("DB 업데이트 후 이미지 URL: {}", updatedMember.getMemberImage());

            // 세션 업데이트
            updateSessionMember(session, member);

            response.put("success",true);
            response.put("message", "회원정보가 성공적으로 수정되었습니다.");

            return response;

        } catch (Exception e) {
            log.error("회원 정보 수정 실패: {}", e.getMessage());
            response.put("error", e.getMessage());
            response.put("message", "회원정보 수정에 실패했습니다.");
            return response;
        }
    }


/*

    @PostMapping("/updateMember")
    @ResponseBody
    public Map<String, Object> updateMember(HttpSession session, Member member,
                                            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        Map<String, Object> response = new HashMap<>();

        try {
            Member sessionMember = (Member) session.getAttribute("member");
            member.setMemberNo(sessionMember.getMemberNo());

            // 현재 상태 로깅
            log.info("프로필 이미지 업데이트 시작");
            log.info("기존 이미지 URL: {}", sessionMember.getMemberImage());

            if (profileImage != null && !profileImage.isEmpty()) {
                try {
                    log.info("새 이미지 파일명: {}", profileImage.getOriginalFilename());
                    String imageUrl = imageService.uploadImage(profileImage);
                    log.info("생성된 이미지 URL: {}", imageUrl);

                    // null 체크 추가
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        member.setMemberImage(imageUrl);
                        log.info("Member 객체에 설정된 이미지 URL: {}", member.getMemberImage());
                    } else {
                        log.error("이미지 URL이 null 또는 비어있음");
                    }
                } catch (IOException e) {
                    log.error("이미지 업로드 중 에러 발생: {}", e.getMessage());
                    response.put("success", false);
                    response.put("message", "이미지 업로드 중 오류가 발생했습니다.");
                    return response;
                }
            } else {
                log.info("새로운 이미지가 업로드되지 않음. 기존 이미지 유지");
                member.setMemberImage(sessionMember.getMemberImage());
            }

            // 업데이트 전 member 객체 상태 확인
            log.info("업데이트 전 member 객체: {}", member.toString());

            if (sessionMember.isSocial()) {
                memberService.updateSocialMember(member);
            } else {
                memberService.updateMember(member);
            }

            // 업데이트 후 DB에서 재조회하여 확인
            Member updatedMember = memberService.getMember(member.getMemberId());
            log.info("DB 업데이트 후 이미지 URL: {}", updatedMember.getMemberImage());

            // 세션 업데이트
            updateSessionMember(session, member);

            response.put("success", true);
            response.put("message", "회원 정보가 성공적으로 수정되었습니다.");

        } catch (Exception e) {
            log.error("회원 정보 수정 실패: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "회원 정보 수정 중 오류가 발생했습니다.");
        }

        return response;
    }
*/

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

        session.setAttribute("member", sessionMember);
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
    public String myPage(Model model, HttpSession session,
                         @RequestParam(required = false) String success,
                         @RequestParam(required = false) String error) {
        Member member = (Member) session.getAttribute("member");
        model.addAttribute("member", member);

        if (success != null) {
            model.addAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
        } else if (error != null) {
            if ("image".equals(error)) {
                model.addAttribute("errorMessage", "이미지 업로드 중 오류가 발생했습니다.");
            } else if ("update".equals(error)) {
                model.addAttribute("errorMessage", "회원 정보 수정 중 오류가 발생했습니다.");
            }
        }

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
        return "redirect:/mainPage";
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
            member.setMemberImage("/images/default-profile.png");



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

        Boolean check = count == 0;
        response.put("available", check);
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