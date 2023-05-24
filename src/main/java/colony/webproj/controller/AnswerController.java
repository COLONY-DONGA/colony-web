package colony.webproj.controller;

import colony.webproj.dto.AnswerFormDto;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AnswerController {

    private final AnswerService answerService;

    /**
     * 답변 생성
     */
    @PostMapping("/answer/{postId}")
    @ResponseBody
    public String saveAnswer(@PathVariable("postId") Long postId,
                             @Valid AnswerFormDto answerFormDto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            /* 글작성 실패시 입력 데이터 값 유지 */
            model.addAttribute("postFormDto", answerFormDto);
            return "postForm";
        }
        /**
         * 로직 추가
         */
        return "답변 생성";
    }
}
