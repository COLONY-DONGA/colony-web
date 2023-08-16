package colony.webproj.controller;

import colony.webproj.dto.CommentDto;
import colony.webproj.dto.CommentFormDto;
import colony.webproj.entity.Role;
import colony.webproj.exception.CustomException;
import colony.webproj.exception.ErrorCode;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 생성
     */
    @PostMapping("/comment/{answerId}")
    public String saveComment(HttpServletRequest request,
                                         @PathVariable("answerId") Long answerId,
                                         CommentFormDto commentFormDto,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("==========   " + commentFormDto.getContent());
        String refer = request.getHeader("Referer"); // 이전 url 주소
        commentService.saveComment(answerId, commentFormDto, principalDetails.getLoginId());
        return "redirect:" + refer;
    }

    /**
     * 대댓글 생성
     */
    @PostMapping("/comment/{answerId}/{commentId}")
    public String saveReComment(HttpServletRequest request,
                                           @PathVariable("answerId") Long answerId,
                                           @PathVariable("commentId") Long commentId,
                                           CommentFormDto commentFormDto,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String refer = request.getHeader("Referer"); // 이전 url 주소

        commentService.saveReComment(answerId, commentId, commentFormDto, principalDetails.getLoginId());
        return "redirect:" + refer;
    }

    @GetMapping("/edit-comment/{commentId}")
    @ResponseBody
    public ResponseEntity<CommentFormDto> updateCommentOrReCommentForm(@PathVariable("commentId") Long commentId,
                                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        //로그인 유저가 작성자와 다를 때
        //admin 은 수정 가능
        if (!principalDetails.getLoginId().equals(commentService.findWriter(commentId)) &&
                principalDetails.getRole() != Role.ROLE_ADMIN) {
            throw new CustomException(ErrorCode.COMMENT_UPDATE_WRONG_ACCESS);
        }
        return ResponseEntity.ok(commentService.findCommentOne(commentId));
    }

    /**
     * 댓글 수정
     * 대댓글 수정
     */
    @PostMapping("/edit-comment/{commentId}")
    @ResponseBody
    public ResponseEntity<?> updateCommentOrReComment(HttpServletRequest request,
                                                            @PathVariable("commentId") Long commentId,
                                                            @RequestBody CommentFormDto commentFormDto,
                                                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String refer = request.getHeader("Referer"); // 이전 url 주소

        //로그인 유저가 작성자와 다를 때
        //admin 은 수정 가능
        if (!principalDetails.getLoginId().equals(commentService.findWriter(commentId)) &&
                principalDetails.getRole() != Role.ROLE_ADMIN) {
            throw new CustomException(ErrorCode.COMMENT_UPDATE_WRONG_ACCESS);
        }
        commentService.updateCommentOrRecomment(commentId, commentFormDto, principalDetails.getLoginId());
        return ResponseEntity.ok(true);
    }

    /**
     * 댓글 삭제
     */
    @GetMapping("/delete-comment/{commentId}")
    public String deleteComment(HttpServletRequest request,
                                           @PathVariable("commentId") Long commentId,
                                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String refer = request.getHeader("Referer"); // 이전 url 주소

        //로그인 유저가 작성자와 다를 때
        //admin 은 수정 가능
        if (!principalDetails.getLoginId().equals(commentService.findWriter(commentId))) {
            throw new CustomException(ErrorCode.COMMENT_DELETE_WRONG_ACCESS);
        }
        commentService.deleteComment(commentId);
        log.info(refer);
        return "redirect:" + refer;

    }

}
