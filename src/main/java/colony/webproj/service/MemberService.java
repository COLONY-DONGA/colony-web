package colony.webproj.service;

import colony.webproj.dto.JoinFormDto;
import colony.webproj.entity.Member;
import colony.webproj.entity.Role;
import colony.webproj.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
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


    public List<Member> findAllMember() {
        List<Member> all = memberRepository.findAll();
        return all;
    }
}
