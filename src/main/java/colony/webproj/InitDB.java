package colony.webproj;

import colony.webproj.entity.*;
import colony.webproj.service.MemberService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initMember();
    }

    @Service
    @RequiredArgsConstructor
    @Transactional
    public static class InitService {
        private final EntityManager em;
        private final BCryptPasswordEncoder encoder;

        public void initMember() {
            Member member1 = Member.builder().loginId("a").password(encoder.encode("a")).role(Role.ROLE_MEMBER)
                    .name("김진수").department("컴퓨터공학과").nickname("김진짜").phoneNumber("01012345678").email("kimjinsu3206@gmail.com").emailAlarm(true).build();
            Member member2 = Member.builder().loginId("b").password(encoder.encode("b")).role(Role.ROLE_MEMBER)
                    .name("채승지").department("컴퓨터공학과").nickname("채똘복").phoneNumber("01012345678").build();
            Member member3 = Member.builder().loginId("c").password(encoder.encode("c")).role(Role.ROLE_MEMBER)
                    .name("박태민").department("컴퓨터공학과").nickname("카사노바").phoneNumber("01012345678").build();
            Member member4 = Member.builder().loginId("d").password(encoder.encode("d")).role(Role.ROLE_MEMBER)
                    .name("박유진").department("컴퓨터공학과").nickname("야경맨").phoneNumber("01012345678").build();
            Member member5 = Member.builder().loginId("e").password(encoder.encode("e")).role(Role.ROLE_MEMBER)
                    .name("최유현").department("컴퓨터공학과").nickname("박유진이남친").phoneNumber("01012345678").build();

            List<Member> members = new ArrayList<Member>();
            members.add(member1);
            members.add(member2);
            members.add(member3);
            members.add(member4);
            members.add(member5);

            em.persist(member1);em.persist(member2);em.persist(member3);em.persist(member4);em.persist(member5);

            List<Post> posts = new ArrayList<Post>();
            for(int i = 1; i<=15; i++) {
                Post post = new Post();
                if (i <= 5) {
                    post = Post.builder().title("제목" + i).content("내용" + i).member(member1).answered(false).build();
                } else if(i<=3) {
                    post = Post.builder().title("제목제목제목제목제목제목제목제목제목제목제목" + i).content("내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용" + i).member(member2).answered(false).build();
                } else if(i<=6) {
                    post = Post.builder().title("제목제목제목제목" + i).content("내용" + i).member(member3).answered(false).build();
                } else if(i<=9) {
                    post = Post.builder().title("제목" + i).content("내용" + i).member(member4).answered(false).build();
                } else if(i<=12) {
                    post = Post.builder().title("제목제목제목제목제목제목제목제목제목제목제목제" + i).content("내내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용" + i).member(member5).answered(false).build();
                } else if(i<=15) {
                    post = Post.builder().title("제목제목제목제목제목제목제목제목제목제목제목제" + i).content("내내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용" + i).member(member2).answered(false).build();
                }

                posts.add(post);
                em.persist(post);
            }
            for(int i = 1; i<=5; i++) {
                Member member_ = Member.builder().loginId("dummyId"+i).password(encoder.encode("abcdefg1!")).role(Role.ROLE_MEMBER)
                        .name("dummyName"+i).department("컴퓨터공학과").nickname("dummyNickname"+i).phoneNumber("01012345678").build();
                em.persist(member_);
            }

            List<Answer> answers = new ArrayList<Answer>();
            for(int i =1; i<=5; i++){
                Answer answer= Answer.builder().content("test"+i).post(posts.get(i-1)).member(member1).build();
                answers.add(answer);
                em.persist(answer);
            }


            for(int i =1; i<=5; i++){
                Comment comment= Comment.builder().content("test comment"+i).answer(answers.get(i-1)).member(member1).build();

                em.persist(comment);
            }


            for (int i =0;i<5;i++){
                Likes likes = Likes.builder().answer(answers.get(0)).member(members.get(i)).build();
                em.persist(likes);
            }
        }
    }
}
