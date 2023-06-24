package colony.webproj.controller;

import colony.webproj.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    /**
     * 단일 이미지 삭제
     * 게시글, 댓글 수정 폼에서 사용
     */
    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable("imageId") Long imageId) {
        imageService.deleteFileOne(imageId);
        return ResponseEntity.ok(true);
    }
}
