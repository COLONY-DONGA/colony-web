package colony.webproj.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException e, Model model) {
        model.addAttribute("errorMessage", e);
        return "errorPage";
    }

    @ExceptionHandler(Exception.class)
    public String handleCustomException(Exception e, Model model) {
        model.addAttribute("errorMessage", e);
        return "errorPage";
    }
}
