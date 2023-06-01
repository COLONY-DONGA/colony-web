package colony.webproj.service;

import colony.webproj.dto.JoinFormDto;
import colony.webproj.dto.MemberDto;
import colony.webproj.dto.MemberFormDto;
import colony.webproj.entity.Member;
import colony.webproj.entity.Role;
import colony.webproj.repository.MemberRepository;
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
     * 마이페이지 멤버 정보 가져오기 (본인의 답변에 대한 모든 좋아요 수를 보여줘야 함.)
     */
    public Member searchMember(String loginID){
        Optional<Member> memberEntity = memberRepository.findMemberWithLikeCount(loginID);
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

    /**
     *  마이페이지 사용자정보 수정
     */
    public Long updateMember(String loginId, MemberFormDto memberFormDto) throws IOException {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        member.setPassword(encoder.encode(memberFormDto.getPassword()));
        member.setName(memberFormDto.getName());
        member.setNickname(memberFormDto.getNickname());
        member.setPhoneNumber(memberFormDto.getPhoneNumber());
        member.setDepartment(memberFormDto.getDepartment());

         return member.getId();
    }
}
