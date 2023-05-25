package colony.webproj.controller;

import colony.webproj.dto.CommentDto;
import colony.webproj.dto.PostDto;
import colony.webproj.dto.PostFormDto;
import colony.webproj.entity.Post;
import colony.webproj.entity.Role;
import colony.webproj.entity.type.SearchType;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.CommentService;
import colony.webproj.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    /**
     * 게시글 리스트
     */
    @GetMapping("/post-list")
    @ResponseBody //데이터 테스트하기 위해서 씀
    public Page<PostDto> postList(@RequestParam(required = false) SearchType searchType,
                           @RequestParam(required = false) String searchValue, // 검색타입과 검색어를 파라미터로 들고와서
                           @RequestParam(required = false) Boolean answered, //답변 유무에 따른 필터
                           @RequestParam(defaultValue = "createdAt") String sortBy, //정렬기준
                           @PageableDefault(size = 10) Pageable pageable,
                           Model model) {
        //승지방식
        Page<PostDto> posts = postService.searchPosts(searchType, searchValue, pageable);
        //진수방식
        Page<PostDto> postDtoList = postService.searchPostList(searchType, searchValue, answered, sortBy, pageable);

        model.addAttribute("postDtoList", postDtoList);
        return postDtoList;
    }

    /**
     * 게시글 상세
     */
    @GetMapping("/post/{postId}")
    @ResponseBody
    public Response postDetail(@PathVariable("postId") Long postId,
                             Model model) {
        List<CommentDto> commentDtoList = commentService.convertNestedStructure(postId); //댓글 가져오기
        model.addAttribute("commentDtoList", commentDtoList);
        PostDto postDto = postService.findPostDetail(postId); //이미지, post관련 데이터 가져오기
        return new Response(commentDtoList, postDto);
    }


    /**
     * 게시글 생성 폼
     */
    @GetMapping("/post-form")
    @ResponseBody
    public String postForm() {
        return "게시글 폼";
    }

    /**
     * 게시글 생성
     */
    @PostMapping("/post")
    @ResponseBody
    public String savePost(@Valid PostFormDto postFormDto, BindingResult bindingResult,
                           @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            /* 글작성 실패시 입력 데이터 값 유지 */
            model.addAttribute("postFormDto", postFormDto);
            return "postForm";
        }
        Long savedPostId = postService.savePost(postFormDto, principalDetails.getUsername());
//        return "redirect:/post/" + savedPostId; //상세 페이지로 이동
        return "게시글 상세";
    }

    /**
     * 게시글 수정 폼
     */
    @GetMapping("/edit-post/{postId}")
    @ResponseBody
    public PostFormDto editFrom(@PathVariable("postId") Long postId, Model model,
                                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        //로그인 유저가 작성자와 다를 때
        //admin 은 수정 가능
        if (!principalDetails.getLoginId().equals(postService.findWriter(postId)) &&
                principalDetails.getRole() != Role.ROLE_ADMIN) {
            return null;
        }
        PostFormDto postFormDto = postService.updateForm(postId);
        model.addAttribute("postFormDto", postFormDto);
        return postFormDto;
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/edit-post/{postId}")
    @ResponseBody
    public String editPost(@PathVariable("postId") Long postId,
                           @Valid PostFormDto postFormDto, BindingResult bindingResult,
                           @AuthenticationPrincipal PrincipalDetails principalDetails,
                           Model model) throws IOException {
        //로그인 유저가 작성자와 다를 때
        //admin 은 수정 가능
        if (!principalDetails.getLoginId().equals(postService.findWriter(postId)) &&
                !principalDetails.getRole().equals(Role.ROLE_ADMIN)) {
            return "에러";
        }
        /* 수정 실패시 입력 데이터 값 유지 */
        if (bindingResult.hasErrors()) {
            model.addAttribute("postFormDto", postFormDto);
            return "게시글 폼";
        }
        postService.updatePost(postId, postFormDto);
        return "게시글 상세";
    }

    /**
     * 댓글 삭제
     * 대댓글 삭제
     */
    @DeleteMapping("/delete-post/{postId}")
    @ResponseBody
    public String deletePost(@PathVariable("postId") Long postId,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "redirect:/login";
        }
        //로그인 유저가 작성자와 다를 때
        //admin 은 수정 가능
        if (!principalDetails.getLoginId().equals(postService.findWriter(postId)) &&
                principalDetails.getRole() != Role.ROLE_ADMIN) {
            return "에러";
        }
        postService.deletePost(postId);
        return "게시글 리스트";
    }

    /**
     * 데이터 잘 뿌려졌는지 확인하기 위해 잠시 만든 클래스
     */
    @Data
    @AllArgsConstructor
    static class Response {
        private List<CommentDto> commentDtoList;
        private PostDto postDto;
    }
}



