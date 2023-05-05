package colony.webproj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Test {

    @ResponseBody
    @GetMapping("/test")
    public String test() {
        return "테스트";
    }
}
