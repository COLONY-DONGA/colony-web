package colony.webproj.security;

import colony.webproj.dto.MemberDto;
import colony.webproj.entity.Member;
import colony.webproj.entity.Role;
import colony.webproj.repository.MemberRepository;
import colony.webproj.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            log.info("loadUserByUsername 메소드 실행");
            Member member = memberRepository.findByLoginId(username);

            MemberDto memberDto = MemberDto.builder()
                    .id(member.getId())
                    .loginId(member.getLoginId())
                    .password(member.getPassword())
                    .name(member.getName())
                    .nickname(member.getNickname())
                    .phoneNumber(member.getPhoneNumber())
                    .department(member.getDepartment())
                    .role(member.getRole()).build();
            log.info("loadUserByUsername 메소드 종료");
            return new PrincipalDetails(memberDto); //세션에 커스텀한 PrincipalDetails 저장
        }

    }
}

