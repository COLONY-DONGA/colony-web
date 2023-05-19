package colony.webproj.controller;

import colony.webproj.dto.MemberDto;
import colony.webproj.service.MemberService;
import colony.webproj.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {
    private final PostService postService;
    private final MemberService memberService;

    // 답변과 댓글은 나중에 구현하겠음. 아직 확정되지 않음


    /**
     * 마이페이지 (일단 내 정보 보여주기만) / 질문1
     */
    @GetMapping("/my-page")
    public String myPage(@RequestParam(value = "loginId") String loginId, Model model){
        MemberDto member = memberService.searchMember(loginId);
        model.addAttribute("member",member);
        return "redirect:/my-page";
    }

    /**
     * 마이페이지 수정 시 패스워드 확인
     */
    @PostMapping("/my-page/validation-password")
    public ResponseEntity<?> validationPassword(@RequestParam(value = "loginId") String loginId, @RequestBody String password){
        // 패스워드 검사 시행
        if(memberService.validationPassword(loginId,password)){
            return ResponseEntity.ok(true); // 200
        }
        else{
            return ResponseEntity.ok(false); // 200
        }
    }

    /**
     * 마이페이지 수정폼 /질문 2
     */

    /**
     * 마이페이지 수정
     */

}
