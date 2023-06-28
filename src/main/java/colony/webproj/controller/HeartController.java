package colony.webproj.controller;

import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.HeartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HeartController {

    // https://github.com/f-lab-edu/chu-chu/blob/develop/src/main/java/com/example/chuchu/board/repository/BoardRepositoryImpl.java 좋아요 기능 개발 시 참고 사이트

    private final HeartService heartService;

    /**
     *  좋아요 추가
     */
    @PostMapping("/heart/{answerId}")
    public ResponseEntity<?> increaseHeart(@PathVariable("answerId") Long answerId,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception{
        boolean isAdd = heartService.addHeart(answerId, principalDetails.getLoginId());
        if(!isAdd) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(true);
    }

    /**
     * 좋아요 취소
     */
    @DeleteMapping("/heart{answerId}")
    public ResponseEntity<?> decreaseHeart(@PathVariable("answerId") Long answerId,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception{
        heartService.removeHeart(answerId, principalDetails.getLoginId());
        return ResponseEntity.ok(true);
    }
}
