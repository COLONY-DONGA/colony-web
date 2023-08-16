package colony.webproj.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class TimeController {
    @GetMapping("/time")
    public ResponseEntity<?> time() {
        return ResponseEntity.ok(LocalDateTime.now());
    }
}
