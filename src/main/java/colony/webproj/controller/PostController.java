package colony.webproj.controller;

import colony.webproj.dto.AnswerDto;
import colony.webproj.dto.CommentDto;
import colony.webproj.dto.PostDto;
import colony.webproj.dto.PostFormDto;
import colony.webproj.entity.Role;
import colony.webproj.entity.type.SearchType;
import colony.webproj.exception.CustomException;
import colony.webproj.exception.ErrorCode;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.AnswerService;
import colony.webproj.service.CommentService;
import colony.webproj.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final AnswerService answerService;

    /**
     * 게시글 리스트
     */
    @GetMapping("/post-list")
    public String postList(@RequestParam(required = false) SearchType searchType,
                           @RequestParam(required = false) String searchValue, // 검색타입과 검색어를 파라미터로 들고와서
                           @RequestParam(required = false) Boolean answered, //답변 유무에 따른 필터
                           @RequestParam(defaultValue = "createdAtDesc") String sortBy, //정렬기준
                           @PageableDefault(size = 3) Pageable pageable,
                           @AuthenticationPrincipal PrincipalDetails principalDetails,
                           Model model) {
        if(principalDetails == null) {
            model.addAttribute("username", "게스트");
            log.info("비회원 로그인");
        }
        else {
            model.addAttribute("username", principalDetails.getNickname());
            log.info("회원 로그인");
        }
        //진수방식
        Page<PostDto> postDtoList = postService.searchPostList(searchType, searchValue, answered, sortBy, pageable);
        model.addAttribute("postDtoList", postDtoList);

        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("answered", answered);

        model.addAttribute("pageNum", pageable.getPageNumber());
        model.addAttribute("totalPages", postDtoList.getTotalPages());
        model.addAttribute("maxPage", 10);

        return "qaList";
    }

    /**
     * 게시글 상세
     */
    @GetMapping("/post/{postId}")
    public String postDetail(@PathVariable("postId") Long postId,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             Model model) {
        PostDto postDto = postService.findPostDetail(postId); //이미지, post 관련 데이터 가져오기
        model.addAttribute("postDto", postDto);

        List<AnswerDto> answerDtoList = answerService.findByPostId(postId); //답변과 댓글, 대댓글, 이미지데이터 가져오기
        model.addAttribute("answerDtoList", answerDtoList);

        if(principalDetails != null) {
            if(principalDetails.getRole() == Role.ROLE_ADMIN) model.addAttribute("loginUser", "관리자");
            else model.addAttribute("loginUser", principalDetails.getNickname());
            model.addAttribute("postUser", postDto.getCreatedBy());
        }
        return "qaDetail";
    }


    /**
     * 게시글 생성 폼
     */
    @GetMapping("/post-form")
    public String postForm() {
        return "qEnroll";
    }

    /**
     * 게시글 생성
     */
    @PostMapping("/post")
    public String savePost(@Valid PostFormDto postFormDto, BindingResult bindingResult,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails,
                                   Model model) throws IOException {
        log.info("포스트 저장 진입");
        if (bindingResult.hasErrors()) {
            /* 글작성 실패시 입력 데이터 값 유지 */
            model.addAttribute("postFormDto", postFormDto);
            return "qEnroll";
        }

        Long savedPostId = postService.savePost(postFormDto, principalDetails.getUsername());
        return "redirect:/post/" + savedPostId; //상세 페이지로 이동
    }


    /**
     * 게시글 수정 폼
     */
    @GetMapping("/edit-post/{postId}")
    public String editFrom(@PathVariable("postId") Long postId, Model model,
                                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        //로그인 유저가 작성자와 다를 때
        //admin 은 수정 가능
        if (!principalDetails.getLoginId().equals(postService.findWriter(postId)) &&
                principalDetails.getRole() != Role.ROLE_ADMIN) {
            throw new CustomException(ErrorCode.POST_DELETE_WRONG_ACCESS);
        }
        PostFormDto postFormDto = postService.updateForm(postId);
        model.addAttribute("postFormDto", postFormDto);
        return "qModify";
    }

    /**
     * 게시글 수정
     */
    @PostMapping("/edit-post/{postId}")
    public String editPost(@PathVariable("postId") Long postId,
                           @Valid PostFormDto postFormDto, BindingResult bindingResult,
                           @AuthenticationPrincipal PrincipalDetails principalDetails,
                           Model model) throws IOException {

        //로그인 유저가 작성자와 다를 때
        //admin 은 수정 가능
        if (!principalDetails.getLoginId().equals(postService.findWriter(postId)) &&
                !principalDetails.getRole().equals(Role.ROLE_ADMIN)) {
            throw new CustomException(ErrorCode.POST_UPDATE_WRONG_ACCESS);
        }
        /* 수정 실패시 입력 데이터 값 유지 */
        if (bindingResult.hasErrors()) {
            model.addAttribute("postFormDto", postFormDto);
            return "qEnroll";
        }
        postService.updatePost(postId, postFormDto);
        return "redirect:/post/" + postId;
    }

    /**
     * 댓글 삭제
     * 대댓글 삭제
     * 답변 삭제
     * 게시글에 대한 이미지, 답변에 대한 이미지 삭제
     */
    @GetMapping("/delete-post/{postId}")
    public String deletePost(@PathVariable("postId") Long postId,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             Model model) {
        if (principalDetails == null) {
            return "redirect:/login";
        }
        //로그인 유저가 작성자와 다를 때
        //admin 은 수정 가능
        if (!principalDetails.getLoginId().equals(postService.findWriter(postId)) &&
                principalDetails.getRole() != Role.ROLE_ADMIN) {
            throw new CustomException(ErrorCode.POST_DELETE_WRONG_ACCESS);
        }
        postService.deletePost(postId);
        return "redirect:/post-list";
    }

    /**
     * 데이터 잘 뿌려졌는지 확인하기 위해 잠시 만든 클래스
     */
    @Data
    @AllArgsConstructor
    static class Response {
        private PostDto postDto;
        private List<AnswerDto> answerDtoList;
    }
}



