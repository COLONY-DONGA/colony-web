package colony.webproj.service;

import colony.webproj.dto.*;
import colony.webproj.entity.Member;
import colony.webproj.entity.Role;
import colony.webproj.exception.CustomException;
import colony.webproj.exception.ErrorCode;
import colony.webproj.repository.memberRepository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    /**
     * 회원가입
     */
    public Long join(JoinFormDto joinFormDto) {
        log.info(joinFormDto.getPassword().toString());
        Member member = Member.builder()
                .loginId(joinFormDto.getLoginId())
                .password(encoder.encode(joinFormDto.getPassword()))
                .name(joinFormDto.getName())
                .nickname(joinFormDto.getNickname())
                .phoneNumber(joinFormDto.getPhoneNumber())
                .department(joinFormDto.getDepartment())
                .role(Role.ROLE_MEMBER)
                .email(joinFormDto.getEmail())
                .emailAlarm((joinFormDto.getEmailAlarm() == null) ? false : joinFormDto.getEmailAlarm())
                .build();
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 아이디 중복 체크
     */
    public Boolean validateLoginId(String loginId) {
        Optional<Member> memberEntity = memberRepository.findByLoginId(loginId);
        return memberEntity.isPresent() ? false : true;
    }

    /**
     * 닉네임 중복 체크
     */
    public Boolean validateNickname(String nickname) {
        Optional<Member> memberEntity = memberRepository.findByNickname(nickname);
        return memberEntity.isPresent() ? false : true;
    }

    /**
     * 이메일 중복 체크
     */
    public Boolean validateEmail(String email) {
        Optional<Member> memberEntity = memberRepository.findByEmail(email);
        return memberEntity.isPresent() ? false : true;
    }


    /**
     * 마이페이지에 필요한 정보 반환
     */
    public MemberPageDto memberPageInfo(String loginId) {
        MemberPageDto memberInfo = memberRepository.findMemberInfo(loginId);
        return memberInfo;
    }

    /**
     * 예전 코드 (나중에 삭제)
     */
    public MyPageDto searchMember(String loginID){
        MyPageDto myPageDto = memberRepository.findMemberWithLikeCount(loginID);
        myPageDto.setLikesCount(memberRepository.countAllAnswerLikes(loginID));
        return myPageDto; // orElse 메서드를 사용하여 Optional이 비어있을 경우에는 null을 반환하도록 처리합니다.
    }

    /**
     * 마이페이지 패스워드 체크
     */
    public Boolean validationPassword(String loginID,String inputPassword){
        // 실제 데이터베이스의 패스워드
        String password = memberRepository.findPasswordByLoginId(loginID);

        if (password != null) {
            Boolean value = encoder.matches(inputPassword, password);
            return value;
        }

        return false;
    }

    /**
     *  마이페이지 사용자정보 수정
     */
    public Long updateMember(String loginId, MemberFormDto memberFormDto) throws IOException {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.setName(memberFormDto.getName());
        member.setPhoneNumber(memberFormDto.getPhoneNumber());
        member.setDepartment(memberFormDto.getDepartment());
        member.setEmail(memberFormDto.getEmail());

        memberRepository.save(member);

         return member.getId();
    }

    /**
     * 마이페이지 사용자 패스워드 수정
     * 현재 패스워드가 맞는 지 확인하고 새로운 패스워드로 전환한다.
     */
    public Long updateMemberPassword(String loginId, PasswordFormDto passwordFormDto) throws IOException{
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if(validationPassword(member.getLoginId(), passwordFormDto.getExisting_password())){
            member.setPassword(encoder.encode(passwordFormDto.getNewPassword()));
        }

        return member.getId();
    }

    /**
     * 이메일 푸쉬 알람 동의 변경
     */
    public void changeEmailAlarmAgree(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.setEmailAlarm(!member.getEmailAlarm());
        log.info("멤버 이메일 푸쉬 알람 동의 변경, 변경 정보: " + member.getEmailAlarm());
    }
}
