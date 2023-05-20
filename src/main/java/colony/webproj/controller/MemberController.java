package colony.webproj.controller;

import colony.webproj.dto.JoinFormDto;
import colony.webproj.dto.LoginFormDto;
import colony.webproj.entity.Member;
import colony.webproj.entity.Role;
import colony.webproj.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String errorMessage,
                            Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", errorMessage);

        return "login";
    }
    /**
     * 비회원 로그인
     */
    @GetMapping("/login-guest")
    public String guestLogin() {
        return "redirect:/posts";
    }
//    /**
//     * 비회원 로그인
//     */
//    @GetMapping("/login-guest")
//    public String guestLogin() {
//        UserDetails guestUser = User.builder()
//                .username("guest_oxigdkrjbgwzeoisghzisejb")
//                .password("guestpassword")
//                .authorities(Role.ROLE_GUEST.toString())
//                .build();
//
//        //토큰 생성
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(guestUser, guestUser.getPassword(), guestUser.getAuthorities());
//
//        //인증 처리
//        //loadUserByUsername 실행되는지 안 되는지 모르겠음
//        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//
//        //컨텍스트 홀더에 authenticate 객체 저장
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
//        return "redirect:/posts";
//    }

    /**
     * 회원가입 폼
     */
    @GetMapping("/join")
    public String joinForm() {
        return "회원가입 폼";
    }

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public String join(@ModelAttribute JoinFormDto joinFormDto) {
        Long savedMemberId = memberService.join(joinFormDto);
        log.info("회원가입 완료 id=" + savedMemberId);
        return "redirect:/login";
    }

    /**
     * 아이디 중복 체크
     */
    @PostMapping("validation-id")
    public ResponseEntity<?> validationId(@RequestBody String loginId) {
        return ResponseEntity.ok(memberService.validateLoginId(loginId));
    }

    /**
     * 닉네임 중복 체크
     */
    @GetMapping("validation-nickname")
    public ResponseEntity<?> validationNickname(@RequestParam(value = "nickname") String nickname) {
        return ResponseEntity.ok(memberService.validateNickname(nickname));
    }
}
