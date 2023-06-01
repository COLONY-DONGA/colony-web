package colony.webproj.controller;

import colony.webproj.dto.HeartDto;
import colony.webproj.entity.Heart;
import colony.webproj.service.HeartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

    /**
     *  좋아요 추가
     */
    @PostMapping("/heart")
    public ResponseEntity<?> increaseHeart(@RequestBody @Valid HeartDto heartDto) throws Exception{
        heartService.addHeart((heartDto));
        return ResponseEntity.ok(true);
    }

    /**
     * 좋아요 취소
     */
    @DeleteMapping("/heart")
    public ResponseEntity<?> decreaseHeart(@RequestBody @Valid HeartDto heartDto) throws Exception{
        heartService.removeHeart(heartDto);
        return ResponseEntity.ok(true);
    }
}
