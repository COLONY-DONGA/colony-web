package colony.webproj;

import colony.webproj.entity.Member;
import colony.webproj.entity.Role;
import colony.webproj.service.MemberService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initMember();
    }

    @Component
    @Service
    @RequiredArgsConstructor
    @Transactional
    static class InitService {
        private final EntityManager em;
        private final BCryptPasswordEncoder encoder;

        public void initMember() {
            for(int i = 0; i<20;i ++){
                Member member = Member.builder()
                        .loginId("abcde")
                        .password(encoder.encode("abcde1!"))
                        .role(Role.ROLE_MEMBER).build();
                em.persist(member);
            }
        }
    }
}
