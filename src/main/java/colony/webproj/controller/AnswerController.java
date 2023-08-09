package colony.webproj.controller;

import colony.webproj.dto.AnswerDto;
import colony.webproj.dto.AnswerFormDto;
import colony.webproj.dto.PostDto;
import colony.webproj.entity.Role;
import colony.webproj.exception.CustomException;
import colony.webproj.exception.ErrorCode;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.AnswerService;
import colony.webproj.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AnswerController {

    private final AnswerService answerService;
    private final PostService postService;

    /**
     * 답변 생성 폼
     * 질문 상세 -> 답변하기 버튼 -> 질문 데이터랑 답변
     */
    @GetMapping("/answer/{postId}")
    public String answerForm(@PathVariable("postId") Long postId, Model model) {
        model.addAttribute("postId", postId);
        return "aEnroll";
    }

    /**
     * 답변 생성
     */
    @PostMapping("/answer/{postId}")
    public String saveAnswer(@PathVariable("postId") Long postId,
                             @Valid AnswerFormDto answerFormDto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             Model model) throws IOException {
        log.info("답변 생성 컨트롤러 진입");
        if (bindingResult.hasErrors()) {
            /* 글작성 실패시 입력 데이터 값 유지 */
            model.addAttribute("answerFormDto", answerFormDto);
            return "aEnroll";
        }
        answerService.saveAnswer(postId, principalDetails.getLoginId(), answerFormDto);

        return "redirect:/post/" + postId;
    }

    /**
     * 답변 수정 폼
     */
    @GetMapping("/edit-answer/{postId}/{answerId}")
    public String editAnswerForm(@PathVariable("postId") Long postId,
                                 @PathVariable("answerId") Long answerId,
                                 @AuthenticationPrincipal PrincipalDetails principalDetails,
                                 Model model) {
        //로그인 유저가 작성자와 다를 때
        //admin 은 수정 가능
        if (!principalDetails.getLoginId().equals(answerService.findWriter(answerId)) &&
                principalDetails.getRole() != Role.ROLE_ADMIN) {
            throw new CustomException(ErrorCode.ANSWER_UPDATE_WRONG_ACCESS);
        }
        //질문 정보
        PostDto postDto = postService.findPostDetail(postId);
        model.addAttribute("postDto", postDto);

        //답변 정보
        AnswerDto answerDto = answerService.findAnswerDetail(answerId);
        model.addAttribute(answerDto);

        return "aModify";
    }

    /**
     * 답변 수정 요청
     * 게시글 상세로 리다이렉트
     */
    @PostMapping("/edit-answer/{postId}/{answerId}")
    public String editAnswer(@PathVariable("answerId") Long answerId,
                             @PathVariable("postId") Long postId,
                             @Valid AnswerFormDto answerFormDto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            /* 글작성 실패시 입력 데이터 값 유지 */
            model.addAttribute("answerFormDto", answerFormDto);
            return "aEnroll";
        }
        answerService.updateAnswer(answerId, answerFormDto);
        return "redirect:/post/" + postId; //답변 수정한 질문 페이지로 이동
    }

    /**
     * 답변 삭제
     */
    @PostMapping("/delete-answer/{postId}/{answerId}")
    public String deleteAnswer(@PathVariable("answerId") Long answerId, @PathVariable("postId") Long postId,
                               @AuthenticationPrincipal PrincipalDetails principalDetails) {

        //로그인 유저가 작성자와 다를 때
        //admin 은 수정 가능
        if (!principalDetails.getLoginId().equals(answerService.findWriter(answerId)) &&
                principalDetails.getRole() != Role.ROLE_ADMIN) {
            throw new CustomException(ErrorCode.ANSWER_DELETE_WRONG_ACCESS);
        }

        answerService.deleteAnswer(answerId, postId);
        return "redirect:/post/" + postId; //답변 수정한 질문 페이지로 이동

    }


    /**
     * 데이터 잘 뿌려졌는지 확인하기 위해 잠시 만든 클래스
     */
    @Data
    @AllArgsConstructor
    static class Response {
        private AnswerDto answerDto;
        private PostDto postDto;
    }
}
