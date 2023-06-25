package colony.webproj.controller;

import colony.webproj.dto.MemberDto;
import colony.webproj.dto.MemberFormDto;
import colony.webproj.dto.MemberWithLikesDto;
import colony.webproj.entity.Member;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.MemberService;
import colony.webproj.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        String loginId = principalDetails.getLoginId();
        MemberWithLikesDto memberDto = memberService.searchMember(loginId);
        model.addAttribute("member", memberDto);    // 좋아요 개수 포함.
        model.addAttribute("posts", memberDto.getPostDto());
        model.addAttribute("answers", memberDto.getAnswerDto());
        model.addAttribute("comments", memberDto.getCommentDto());

        return "/my-page/detail";
    }

    /**
     * 마이페이지 수정 시 패스워드 확인
     */
    @GetMapping("/my-page/validation-password")
    public ResponseEntity<?> validationPassword(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody String password) {
        String loginId = principalDetails.getLoginId();
        // 패스워드 검사 시행
        if (memberService.validationPassword(loginId, password)) { // 이 때 패스워드는 사용자 입력값임
            return ResponseEntity.ok(true); // 200
        } else {
            return ResponseEntity.ok(false); // 200
        }
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
