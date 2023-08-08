package colony.webproj.controller;

import colony.webproj.dto.MemberFormDto;
import colony.webproj.dto.MyPageDto;
import colony.webproj.dto.PasswordFormDto;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.MemberService;
import jakarta.validation.Valid;
import lombok.*;
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
        String loginId = principalDetails.getLoginId();
        MyPageDto myPageDto = memberService.searchMember(loginId);
        model.addAttribute("member", myPageDto);    // 좋아요 개수 포함.
        model.addAttribute("posts", myPageDto.getPostDto());
        model.addAttribute("answers", myPageDto.getAnswerDto());
        model.addAttribute("comments", myPageDto.getCommentDto());

        return "memberPage";

    }

    /**
     * 마이페이지 수정 시 패스워드 확인
     */
    @PostMapping("/my-page/validation-password")
    public ResponseEntity<?> validationPassword(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody PasswordRequest passwordRequest) {
        String loginId = principalDetails.getLoginId();
        String password = passwordRequest.getPassword();

        Boolean isValid =memberService.validationPassword(loginId, password);
        if(!isValid) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(true);
    }

    /**
     * 마이페이지 수정
     */
    @PutMapping("/edit-mypage")
    public ResponseEntity<?> editMyPage(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestBody @Valid MemberFormDto MemberFormDto,
                             BindingResult bindingResult) throws IOException {
        String loginId = principalDetails.getLoginId();
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        memberService.updateMember(loginId, MemberFormDto);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/edit-password")
    public ResponseEntity<?> editPassword(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody @Valid PasswordFormDto passwordFormDto, BindingResult bindingResult) throws IOException{
        String loginId = principalDetails.getLoginId();
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();

        if(!passwordFormDto.getNewPassword().equals(passwordFormDto.getNewPasswordConfirm()))
            return ResponseEntity.badRequest().body("신규 패스워드가 일치하지 않습니다.");

        memberService.updateMemberPassword(loginId,passwordFormDto);
        return ResponseEntity.ok(true);
    }



    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordRequest {
        private String password;

    }


}
