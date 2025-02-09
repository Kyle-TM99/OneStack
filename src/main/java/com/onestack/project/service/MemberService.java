package com.onestack.project.service;

import com.onestack.project.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onestack.project.mapper.MemberMapper;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final JavaMailSender mailSender;

    // 전문가가 받은 견적 요청 리스트 Estimation
    public List<Estimation> proEstimation(int proNo) {
        try {
            return memberMapper.proEstimation(proNo);
        } catch (Exception e) {
            log.error("견적 요청 목록 조회 실패: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 회원이 요청한 견적 리스트 Estimation
    public List<Estimation> memberEstimation(int memberNo) {
        log.info("Fetching estimations for member: {}", memberNo);
        List<Estimation> result = memberMapper.memberEstimation(memberNo);
        log.info("Found {} estimations", result != null ? result.size() : 0);
        return result;
    }


    // 회원별 게시글 수 조회
    public int memberMyPageCommunityCount(int memberNo) {
        return memberMapper.memberMyPageCommunityCount(memberNo);
    }



    // 회원별 댓글 수 조회
    public int memberMyPageComReplyCount(int memberNo) {
        return memberMapper.memberMyPageComReplyCount(memberNo);
    }


    // 회원별 답글 수 조회
    public int memberMyPageQnAReplyCount(int memberNo) {
        return memberMapper.memberMyPageQnAReplyCount(memberNo);
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
            memberMapper.updateSocialMember(member);
            log.info("Member update completed for memberNo: {}", member.getMemberNo());
        } catch (Exception e) {
            log.error("Member update failed", e);
            throw e;
        }
    }


    public void updateSocialMember(Member member) {
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

            memberMapper.updateSocialMember(member);
            log.info("Member update completed for memberNo: {}", member.getMemberNo());
        } catch (Exception e) {
            log.error("Member update failed", e);
            throw e;
        }
    }


    public boolean changePassword(String memberId, String currentPassword, String newPassword) {
        // 현재 비밀번호 검증
        Member member = memberMapper.getMember(memberId);

        // 현재 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(currentPassword, member.getPass())) {
            return false;
        }

        // 새 비밀번호 암호화
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        try {
            // 비밀번호 업데이트
            memberMapper.updatePassword(memberId, encodedNewPassword);
            return true;
        } catch (Exception e) {
            return false;
        }
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
        // 먼저 소셜 로그인 회원인지 확인
        String socialType = findSocialMemberId(member);

        if (socialType != null) {
            // 소셜 로그인 회원이면 null 반환
            return null;
        }

        // 소셜 로그인 회원이 아니면 아이디 찾기
        return memberMapper.findMemberId(member);
    }

    // 소셜 로그인 회원 확인 메서드 추가
    public String findSocialMemberId(Member member) {
        return memberMapper.findSocialMemberId(member);
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

            String resetLink = "http://www.onestack.store/resetPassword?token=" + token;
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


    // 견적 상태 업데이트
    public void updateEstimationProgress(int estimationNo, int progress) {
        try {
            memberMapper.updateEstimationProgress(estimationNo, progress);
        } catch (Exception e) {
            log.error("견적 상태 업데이트 실패: {}", e.getMessage());
            throw new RuntimeException("견적 상태 업데이트에 실패했습니다.", e);
        }
    }

    // 전체 견적 수 조회
    public int getEstimationCount(int proNo) {
        return memberMapper.getEstimationCount(proNo);
    }

    // 페이지별 견적 목록 조회
    public List<Estimation> getEstimationsByPage(int proNo, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return memberMapper.getEstimationsByPage(proNo, offset, pageSize);
    }

    // 견적 번호로 견적 정보 조회
    public Estimation getEstimationByNo(int estimationNo) {
        return memberMapper.getEstimationByNo(estimationNo);
    }

    // 회원 번호로 회원 정보 조회
    public Member getMemberByNo(int memberNo) {
        return memberMapper.getMemberByNo(memberNo);
    }

    @Transactional
    public void confirmEstimationByPro(int estimationNo) {
        Estimation estimation = memberMapper.getEstimationByNo(estimationNo);
        if (estimation == null) {
            throw new RuntimeException("견적을 찾을 수 없습니다.");
        }

        // 전문가 확인 상태로 변경
        memberMapper.updateEstimationCheck(estimationNo, 1);
    }

    @Transactional
    public void confirmEstimationByClient(int estimationNo) {
        Estimation estimation = memberMapper.getEstimationByNo(estimationNo);
        if (estimation == null) {
            throw new RuntimeException("견적을 찾을 수 없습니다.");
        }

        // 의뢰인 확인 완료 및 결제 단계로 변경
        memberMapper.updateEstimationProgress(estimationNo, 2);  // 결제 단계로 변경
    }

    public void updateEstimationPrice(int estimationNo, int estimationPrice) {
        memberMapper.updateEstimationPrice(estimationNo, estimationPrice);
    }

    // 전문가 수 조회
    public int getProCount() {
        return memberMapper.getProCount();
    }

    // 회원 수 조회
    public int getMemberCount() {
        return memberMapper.getMemberCount();
    }

    // 완료된 외주 수 조회
    public int getMainEstimationCount() {
        return memberMapper.getMainEstimationCount();
    }

    // 전문가 번호 조회
    public int getProNo(int memberNo) {
        Integer proNo = memberMapper.getProNo(memberNo);
        return proNo != null ? proNo : 0;  // null인 경우 0 반환
    }
}
