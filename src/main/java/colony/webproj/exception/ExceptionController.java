package colony.webproj.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

@ControllerAdvice
@Slf4j
public class ExceptionController {


    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException e, Model model) {
        model.addAttribute("errorMessage", e.getErrorMessage());
        model.addAttribute("status", e.getStatus().toString());
        log.info("핸들링한 에러 발생");
        return "errorPage";
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public void handleAsyncRequestTimeoutException() {
        // AsyncRequestTimeoutException 발생 시 아무런 동작도 수행하지 않음
    }

    @ExceptionHandler(Exception.class)
    public String handleCustomException(Exception e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        log.info("핸들링하지 않은 에러 발생");
        log.info("exception: " + e);
        return "errorPage";
    }
}
