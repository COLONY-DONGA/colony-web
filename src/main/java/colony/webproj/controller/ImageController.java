package colony.webproj.controller;

import colony.webproj.exception.CustomException;
import colony.webproj.exception.ErrorCode;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

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
    public ResponseEntity<?> deleteImage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @PathVariable("imageId") Long imageId) {
        //로그인 회원과 이미지 등록 회원 검증
        if(!imageService.validateImageAndMember(principalDetails.getId(), imageId)) {
            throw new CustomException(ErrorCode.IMAGE_DELETE_WRONG_ACCESS);
        }
        imageService.deleteFileOne(imageId);
        return ResponseEntity.ok(true);
    }

    @ResponseBody
    @GetMapping("/images/{filename}") //사진 불러오기
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + imageService.getFullPath(filename));
    }
}
