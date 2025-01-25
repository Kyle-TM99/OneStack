package com.onestack.project.service;

import com.onestack.project.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onestack.project.mapper.MemberMapper;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final JavaMailSender mailSender;

    // myPage 내역 조회
    public List<MemberWithProfessional> memberWithProfessional(int memberNo) {
        List<MemberWithProfessional> result = memberMapper.memberWithProfessional(memberNo);
        return result;
    }

    // 회원별 review 리스트 조회
    public List<Review> findMyReview(int memberNo) {
        List<Review> result = memberMapper.findMyReview(memberNo);
        return result;
    }

    // 회원별 게시글 리스트 조회
    public List<MemberWithCommunity> memberMyPageCommunity(int memberNo) {
        List<MemberWithCommunity> result = memberMapper.memberMyPageCommunity(memberNo);
        if (result != null) {
            result.forEach(item -> {
                if (item != null) {
                    System.out.println("Member: " + item.getMember());
                    System.out.println("Community: " + item.getCommunity());
                }
            });
        }
        return result;
    }

    // 회원별 게시글 수 조회
    public int memberMyPageCommunityCount(int memberNo) {
        return memberMapper.memberMyPageCommunityCount(memberNo);
    }

    // 회원별 질문글 조회
    public List<MemberWithQnA> memberMyPageQnA(int memberNo) {
        List<MemberWithQnA> result = memberMapper.memberMyPageQnA(memberNo);
        if (result != null) {
            result.forEach(item -> {
            });
        }
        return result;
    }

    // 회원별 질문글 수 조회
    public int memberMyPageQnACount(int memberNo) {
        return memberMapper.memberMyPageQnACount(memberNo);
    }

    // 회원별 댓글 조회
    public List<ComWithComReply> comWithComReply(int memberNo) {
        return memberMapper.comWithComReply(memberNo);
    }

    // 회원별 댓글 수 조회
    public int memberMyPageComReplyCount(int memberNo) {
        return memberMapper.memberMyPageComReplyCount(memberNo);
    }

    // 회원별 답글 조회
    public List<QnAWithReply> qnaWithReply(int memberNo) {
        return memberMapper.qnaWithReply(memberNo);
    }

    // 회원별 답글 수 조회
    public int memberMyPageQnAReplyCount(int memberNo) {
        return memberMapper.memberMyPageQnAReplyCount(memberNo);
    }

    // 게시글 공감 조회
    public List<Community> memberMyPageCommunityLike(int memberNo) {
        return memberMapper.memberMyPageCommunityLike(memberNo);
    }

    // 질문글 공감 조회
    public List<QnA> memberMyPageQnALike(int memberNo) {
        return memberMapper.memberMyPageQnALike(memberNo);
    }

    // 댓글 공감 조회
    public List<CommunityReply> memberMyPageComReplyLike(int memberNo) {
        try {
            log.info("Fetching comment likes for member: {}", memberNo);
            List<CommunityReply> result = memberMapper.memberMyPageComReplyLike(memberNo);
            log.info("Found {} liked comments", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error fetching comment likes", e);
            throw e;
        }
    }

    // 답변 공감 조회
    public List<QnAReply> memberMyPageQnAReplyLike(int memberNo) {
        return memberMapper.memberMyPageQnAReplyLike(memberNo);
    }


    public void updateMember(Member member) {
        try {
            log.info("Updating member: {}", member);

            // null 체크 추가
            if (member == null) {
                throw new IllegalArgumentException("업데이트할 회원 정보가 없습니다.");
            }

            // 필수 필드 null 체크
            if (member.getMemberNo() == 0) {
                throw new IllegalArgumentException("회원 번호가 유효하지 않습니다.");
            }

            memberMapper.updateMember(member);
            log.info("Member update completed for memberNo: {}", member.getMemberNo());
        } catch (Exception e) {
            log.error("Member update failed", e);
            throw e;
        }
    }

    public void updateMemberMyPagePass(Member member) {
        try {

            // null 체크 추가
            if (member == null) {
                throw new IllegalArgumentException("업데이트할 회원 정보가 없습니다.");
            }

            // 필수 필드 null 체크
            if (member.getMemberNo() == 0) {
                throw new IllegalArgumentException("회원 번호가 유효하지 않습니다.");
            }

            memberMapper.updateMemberMyPagePass(member);
        } catch (Exception e) {
            log.error("Member update failed", e);
            throw e;
        }
    }


/*    // 선택적 필드 업데이트 메서드 추가
    public void updateMemberSelective(Member member) {
        memberMapper.updateMemberSelective(member);
    }
    */



    public List<Map<String, Object>> myPageLikedCommunity(int memberNo) {
        return memberMapper.myPageLikedCommunity(memberNo);
    }

    public List<Map<String, Object>> myPageLikedQnA(int memberNo) {
        return memberMapper.myPageLikedQnA(memberNo);
    }

    public List<Map<String, Object>> myPageLikedCommunityReply(int memberNo) {
        return memberMapper.myPageLikedCommunityReply(memberNo);
    }

    public List<Map<String, Object>> myPageLikedQnAReply(int memberNo) {
        return memberMapper.myPageLikedQnAReply(memberNo);
    }


    // 회원 로그인 요청을 처리하고 결과를 반환하는 메서드
    public int login(String memberId, String pass) {
        int result = -1;
        Member m = memberMapper.getMember(memberId);
        if(m == null) {
            return result;
        }
        if(passwordEncoder.matches(pass, m.getPass())) {
            result = 1;
        } else {
            result = 0;
        }
        return result;
    }

    // 마이페이지

    // 회원 로그인 요청을 처리하기 위한 메서드
    public Member getMember(String memberId) {
        return memberMapper.getMember(memberId);
    }

    public int insertMember(Member member) {
        return memberMapper.insertMember(member);
    }

    public int checkMemberId(String memberId) {
        return memberMapper.checkMemberId(memberId);
    }

    public int checkNickname(String nickname) {
        return memberMapper.checkNickname(nickname);
    }
    public int checkEmail(String email) {
        return memberMapper.checkEmail(email);
    }

    public int checkPhone(String phone) {
        return memberMapper.checkPhone(phone);
    }

    public String findMemberId(Member member) {

        // 아이디 찾기 실행
        return memberMapper.findMemberId(member);
    }

    public List<Review> myPageReview(int memberNo) {
        return memberMapper.findMyReview(memberNo);
    }

    public int findMyReviewCount(int memberNo) {
        return memberMapper.findMyReviewCount(memberNo);
    }


    public boolean memberPassCheck(String memberId, String pass) {
        // DB에서 저장된 비밀번호 가져오기
        String dbPass = memberMapper.memberPassCheck(memberId);

        // 비밀번호가 없는 경우
        if(dbPass == null) {
            return false;
        }

        // 입력한 비밀번호와 DB의 암호화된 비밀번호 비교
        return passwordEncoder.matches(pass, dbPass);
    }

    public int updatePasswordMember(Member member) {
        // 비밀번호 암호화
        member.setPass(passwordEncoder.encode(member.getPass()));
        return memberMapper.updatePasswordMember(member);
    }

    public void sendPasswordResetEmail(String memberId, String email) {
        log.info("비밀번호 재설정 요청 - memberId: {}, email: {}", memberId, email);
        
        Member member = memberMapper.getMember(memberId);
        log.info("조회된 회원 정보: {}", member);

        if (member == null) {
            log.error("회원 정보가 없음 - memberId: {}", memberId);
            throw new RuntimeException("일치하는 회원 정보가 없습니다.");
        }
        
        if (!email.equals(member.getEmail())) {
            log.error("이메일 불일치 - 입력: {}, DB: {}", email, member.getEmail());
            throw new RuntimeException("일치하는 회원 정보가 없습니다.");
        }

        if (member.isSocial()) {
            log.error("소셜계정은 비밀번호 변경할 수 없습니다. - 소셜 여부: {}", memberId);
            throw new RuntimeException("일치하는 회원 정보가 없습니다.");
        }
        
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(memberId, token);
        
        try {
            memberMapper.savePasswordResetToken(resetToken);
            
            String resetLink = "http://localhost:8080/resetPassword?token=" + token;
            String emailContent = String.format(
                "안녕하세요,\n\n" +
                "비밀번호 재설정을 위한 링크입니다:\n%s\n\n" +
                "이 링크는 24시간 동안 유효합니다.\n" +
                "본인이 요청하지 않았다면 이 이메일을 무시하시기 바랍니다.",
                resetLink
            );

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("rlaxoals9977@gmail.com");
            message.setTo(email);
            message.setSubject("OneStack 비밀번호 재설정");
            message.setText(emailContent);
            mailSender.send(message);
            
            log.info("비밀번호 재설정 이메일 발송 완료 - email: {}", email);
        } catch (Exception e) {
            log.error("이메일 발송 실패", e);
            throw new RuntimeException("비밀번호 재설정 이메일 발송에 실패했습니다.");
        }
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = memberMapper.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            throw new RuntimeException("유효하지 않거나 만료된 토큰입니다.");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        memberMapper.updatePassword(resetToken.getMemberId(), encodedPassword);
        memberMapper.deletePasswordResetToken(token);
    }


    
    public Integer getMemberById(String memberId) {
        int memberNo = memberMapper.findMemberNoByMemberId(memberId);
        return memberNo;
    }

}
