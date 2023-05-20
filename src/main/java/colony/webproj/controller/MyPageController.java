package colony.webproj.controller;

import colony.webproj.dto.MemberDto;
import colony.webproj.dto.MemberFormDto;
import colony.webproj.entity.Member;
import colony.webproj.service.MemberService;
import colony.webproj.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     */
    @PostMapping("/my-page")
    public String myPage(@RequestBody String loginId, Model model){
        Member member = memberService.searchMember(loginId);
        model.addAttribute("member",member);
        model.addAttribute("posts",member.getMyPosts());
        model.addAttribute("comments",member.getMyComments());
        return "redirect:/my-page";
    }

    /**
     * 마이페이지 수정 시 패스워드 확인
     */
    @PostMapping("/my-page/validation-password")
    public String validationPassword(@RequestBody Map<String,String> requestBody){
        // 패스워드 검사 시행
        if(memberService.validationPassword(requestBody.get("loginId"),requestBody.get("password"))){ // 이 때 패스워드는 사용자 입력값임
            return "/edit-mypage"; // 200
        }
        else{
            return "redirect:/my-page"; // 200
        }
    }

//    /**
//     * 마이페이지 수정폼
//     */
//    @PostMapping("/edit-mypage")
//    public String editMyPageForm(@RequestBody String loginId,Model model){
//        MemberDto memberDto = memberService.searchMember(loginId);
//        // 프론트 측에 패스워드 부분 비워두라고 하기
//        return "일단은 패스";
//    }

    /**
     * 마이페이지 수정
     */
    @PutMapping("/edit-mypage")
    public String editMyPage(@RequestBody String loginId, @Valid MemberFormDto MemberFormDto,
                             BindingResult bindingResult,Model model) throws IOException {
        if(bindingResult.hasErrors()){
            model.addAttribute("memberFormDto",MemberFormDto);
            return "";
        }
        memberService.updateMember(loginId,MemberFormDto);
        return "/my-page";
    }

}
