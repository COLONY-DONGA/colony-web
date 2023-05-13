package colony.webproj.controller;

import colony.webproj.dto.PostDto;
import colony.webproj.dto.PostFormDto;
import colony.webproj.entity.Post;
import colony.webproj.entity.Role;
import colony.webproj.entity.type.SearchType;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시글 리스트
     */
    @GetMapping("/post-list")
    @ResponseBody
    public String postList(@RequestParam(required = false) SearchType searchType,
                           @RequestParam(required = false) String searchValue, // 검색타입과 검색어를 파라미터로 들고와서
                           @PageableDefault(size = 10) Pageable pageable,
                           @RequestParam
                           Model model) {
        Page<PostDto> posts = postService.searchPosts(searchType, searchValue, pageable);

        model.addAttribute("posts", posts);
        return "postList";
    }

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
                           @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            /* 글작성 실패시 입력 데이터 값 유지 */
            model.addAttribute("postFormDto", postFormDto);
            return "postForm";
        }
        Long savedPostId = postService.savePost(postFormDto, principalDetails.getLoginId());
        return "redirect:/post/" + savedPostId; //상세 페이지로 이동
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
        if (!principalDetails.getLoginId().equals(postService.findWriter(postId)) &&
                !principalDetails.getRole().equals(Role.ROLE_ADMIN)) {
            return "error/404";
        }
        /* 수정 실패시 입력 데이터 값 유지 */
        if (bindingResult.hasErrors()) {
            model.addAttribute("postFormDto", postFormDto);
            return "postForm";
        }

//        postService.updatePost(postId, postFormDto)
        return null;
    }
}
