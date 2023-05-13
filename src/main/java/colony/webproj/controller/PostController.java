package colony.webproj.controller;

import colony.webproj.dto.PostFormDto;
import colony.webproj.entity.Role;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시글 폼
     */
    @GetMapping("/post-form")
    public String postForm(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "redirect:/login";
        }
        return "postForm";
    }

    /**
     * 게시글 생성
     */
    @PostMapping("/post")
    public String savePost(@Valid PostFormDto postFormDto, BindingResult bindingResult,
                           @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if(bindingResult.hasErrors()) {
            /* 글작성 실패시 입력 데이터 값 유지 */
            model.addAttribute("postFormDto", postFormDto);
            return "postForm";
        }
        postService.savePost(postFormDto, principalDetails.getLoginId());
        return "redirect:/postList";
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/edit-post/{postId}")
    public String editPost(@PathVariable("postId") Long postId,
                           @Valid PostFormDto postFormDto, BindingResult bindingResult,
                           @AuthenticationPrincipal PrincipalDetails principalDetails,
                           Model model) {
        /* 로그인 유저와 작성자가 다를 때 */
        /* admin 유저일 경우는 배제 */
        if(!principalDetails.getLoginId().equals(postService.findWriter(postId)) &&
                !principalDetails.getRole().equals(Role.ROLE_ADMIN)) {
            return "error/404";
        }
        /* 수정 실패시 입력 데이터 값 유지 */
        if(bindingResult.hasErrors()) {
            model.addAttribute("postFormDto", postFormDto);
            return "postForm";
        }

//        postService.updatePost(postId, postFormDto)
        return null;
    }
}
