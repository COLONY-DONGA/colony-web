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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername 메소드 실행");
        Optional<Member> findMember = memberRepository.findByLoginId(username);
        if (!findMember.isPresent()) {
            throw new UsernameNotFoundException("가입되지 않은 회원입니다."); //스프링 내부에서 BadCredentialsException 예외로 변형시켜버림
        }
        Member member = findMember.get();

        log.info("loadUserByUsername 메소드 종료");
        return new PrincipalDetails(member.getLoginId(), member.getPassword(), member.getRole()); //세션에 커스텀한 PrincipalDetails 저장

    }
}

