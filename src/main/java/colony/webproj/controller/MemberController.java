package colony.webproj.controller;

import colony.webproj.category.dto.CategoryDto;
import colony.webproj.category.service.CategoryService;
import colony.webproj.dto.JoinFormDto;
import colony.webproj.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final CategoryService categoryService;

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
    public String guestLogin(Model model) {
        return "redirect:/post-list/Q&A";
    }


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

    /**
     * 이메일 중복 체크
     */
    @PostMapping("validation-email")
    public ResponseEntity<?> validationEmail(@RequestBody Validation validation) {
        Boolean isValid = memberService.validateEmail(validation.getEmail());
        if(!isValid) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(true);
    }

    // 접근 거부 처리
    @GetMapping("/denied-page")
    public String showDeniedPage(@RequestParam("type") String type, Model model) {
        if(type.equals("UNAUTHORIZED")) {
            model.addAttribute("msg", "접근 권한이 없습니다.");
        }
        if(type.equals("guest")) {
            model.addAttribute("msg", "로그인 후 이용가능합니다.");
            model.addAttribute("nextPage", "/login");
        }

        return "deniedPage";
    }

    // 댓글 접근 거부 처리
    @GetMapping("/denied-comment")
    @ResponseBody
    public ResponseEntity<?> deniedCommentHandler() {
        return ResponseEntity.badRequest().build();
    }


    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Validation {
        String loginId;
        String nickname;
        String email;
    }
}
