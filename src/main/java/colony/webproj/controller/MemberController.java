package colony.webproj.controller;

import colony.webproj.dto.JoinFormDto;
import colony.webproj.dto.LoginFormDto;
import colony.webproj.entity.Member;
import colony.webproj.entity.Role;
import colony.webproj.service.MemberService;
import lombok.*;
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
<<<<<<< HEAD
    /**
     * 비회원 로그인
     */
    @GetMapping("/login-guest")
    public String guestLogin() {
        model.addAttribute("member","guest");
        return "redirect:/posts";
    }
=======
>>>>>>> main

    /**
     * 회원가입 폼
     */
    @GetMapping("/join")
    public String joinForm() {
        return "register";
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
    public ResponseEntity<?> validationId(@RequestBody Validation validation) {
        Boolean isValid = memberService.validateLoginId(validation.getLoginId());
        if(!isValid) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(true);
    }

    /**
     * 닉네임 중복 체크
     */
    @PostMapping("validation-nickname")
    public ResponseEntity<?> validationNickname(@RequestBody Validation validation) {
        Boolean isValid = memberService.validateNickname(validation.getNickname());
        if(!isValid) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(true);
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Validation {
        String loginId;
        String nickname;
    }
}
