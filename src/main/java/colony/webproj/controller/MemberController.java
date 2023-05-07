package colony.webproj.controller;

import colony.webproj.dto.JoinFormDto;
import colony.webproj.dto.LoginFormDto;
import colony.webproj.entity.Member;
import colony.webproj.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String errorMessage,
                            Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", errorMessage);
        return "login";
    }

    @ResponseBody
    @GetMapping("/")
    public List<Member> a() {
        List<Member> allMember = memberService.findAllMember();
        return allMember;
    }

//
//    @PostMapping("/login")
//    public String login(@ModelAttribute LoginFormDto loginFormDto) {
//        log.info("ㅇㅇ");
//        log.info(loginFormDto.getLoginId());
//        log.info(loginFormDto.getPassword());
//        return "로그인완료";
//    }

    @PostMapping("/join")
    public String join(@ModelAttribute JoinFormDto joinFormDto) {
        Long savedMemberId = memberService.join(joinFormDto);
        log.info("회원가입 완료 id=" + savedMemberId);
        return "redirect:/login";
    }



}
