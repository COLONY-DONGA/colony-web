package colony.webproj.controller;

import colony.webproj.dto.MemberFormDto;
import colony.webproj.dto.MyPageDto;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {
    private final MemberService memberService;

    /**
     * 마이페이지 (일단 내 정보 보여주기만) / 질문1
     * 답변이랑 댓글 확정
     */
    @GetMapping("/my-page")
    public String myPage(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        /**
         *  To do 비회원일 때 처리하는 법 나머지 메소드도.
         */
        String loginId = principalDetails.getLoginId();
        MyPageDto myPageDto = memberService.searchMember(loginId);
        model.addAttribute("member", myPageDto);    // 좋아요 개수 포함.
        model.addAttribute("posts", myPageDto.getPostDto());
        model.addAttribute("answers", myPageDto.getAnswerDto());
        model.addAttribute("comments", myPageDto.getCommentDto());

        return "/memberPage";
    }

    /**
     * 마이페이지 수정 시 패스워드 확인
     */
    @PostMapping("/my-page/validation-password")
    public ResponseEntity<?> validationPassword(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody String password) {
        String loginId = principalDetails.getLoginId();

        Boolean isValid =memberService.validationPassword(loginId, password);
        log.info("isValid:"+isValid);
        if(!isValid) {
            log.info("isValid: if 조건 함수 내부 진입");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(true);
    }


    /**
     * 마이페이지 수정
     */
    @PutMapping("/edit-mypage")
    public String editMyPage(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid MemberFormDto MemberFormDto,
                             BindingResult bindingResult, Model model) throws IOException {
        String loginId = principalDetails.getLoginId();
        if (bindingResult.hasErrors()) {
            model.addAttribute("memberFormDto", MemberFormDto);
            return "redirect:/my-page";
        }
        memberService.updateMember(loginId, MemberFormDto);
        return "/my-page";
    }

}
