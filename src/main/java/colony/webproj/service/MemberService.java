package colony.webproj.service;

import colony.webproj.dto.JoinFormDto;
import colony.webproj.dto.MemberDto;
import colony.webproj.entity.Member;
import colony.webproj.entity.Role;
import colony.webproj.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Member member = Member.builder()
                .loginId(joinFormDto.getLoginId())
                .password(encoder.encode(joinFormDto.getPassword()))
                .name(joinFormDto.getName())
                .nickname(joinFormDto.getNickname())
                .phoneNumber(joinFormDto.getPhoneNumber())
                .department(joinFormDto.getDepartment())
                .role(Role.ROLE_MEMBER)
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

    public List<Member> findAllMember() {
        List<Member> all = memberRepository.findAll();
        return all;
    }

    /**
     * 마이페이지 멤버 정보 가져오기
     */
    public MemberDto searchMember(String loginID){
        Optional<MemberDto> memberEntity = memberRepository.findByLoginId(loginID).map(MemberDto::from);
        return memberEntity.orElse(null); // orElse 메서드를 사용하여 Optional이 비어있을 경우에는 null을 반환하도록 처리합니다.
    }

    /**
     * 마이페이지 패스워드 체크
     */
    public Boolean validationPassword(String loginID,String inputPassword){
        // 실제 데이터베이스의 패스워드
        String password = memberRepository.findPasswordByLoginId(loginID);

        if (password != null) {
            return encoder.matches(inputPassword, password);
        }

        return false;
    }

}
