package colony.webproj.controller;

import colony.webproj.dto.CommentFromDto;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 생성
     */
    @PostMapping("/comment/{boardId}")
    public ResponseEntity<?> saveComment(@PathVariable("boardId") Long boardId,
                                         CommentFromDto commentFormDto,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return ResponseEntity.badRequest().build();
        }
        Long savedCommentId = commentService.saveComment(boardId, commentFormDto, principalDetails.getLoginId());
        return ResponseEntity.ok(true);
    }

//    /**
//     * 대댓글 생성
//     */
//    @PostMapping("/comment/{postId}/{commentId}")
//    public ResponseEntity<?> saveReComment(@PathVariable("postId") Long boardId,
//                                           @PathVariable("commentId") Long commentId,
//                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
//
//    }
}

/*
// AJAX 요청
  var httpRequest = new XMLHttpRequest();
  httpRequest.open("POST", "/comment/" + boardId);
  httpRequest.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  httpRequest.onreadystatechange = function () {
    if (httpRequest.readyState === XMLHttpRequest.DONE) {
      if (httpRequest.status === 200) {
        var response = JSON.parse(httpRequest.responseText);
        if (response === true) {
          // 작업이 성공한 경우
          // 처리할 로직 추가
        } else {
          // 작업이 실패한 경우
          // 처리할 로직 추가
        }
      } else if (httpRequest.status === 400) {
        // 로그인이 필요한 경우
        var response = JSON.parse(httpRequest.responseText);
        var message = response.message;
        var loginUrl = response.loginUrl;
        // 처리할 로직 추가
      } else {
        // 다른 상태 코드 처리
        // 처리할 로직 추가
      }
    }
  };
  httpRequest.send(JSON.stringify(data));
}
 */
