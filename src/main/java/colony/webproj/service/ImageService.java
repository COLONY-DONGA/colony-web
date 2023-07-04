package colony.webproj.service;

import colony.webproj.entity.Image;
import colony.webproj.repository.imageRepository.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${file.dir}")
    private String fileDir;

    /**
     * 이미지 업로드
     */
    public List<Image> uploadFile(List<MultipartFile> multipartFiles) throws IOException {
        List<Image> fileList = new ArrayList<>();
        /* 비었는지 체크 */
        if (!CollectionUtils.isEmpty(multipartFiles)) {
            for (MultipartFile multipartFile : multipartFiles) {
                String extension; //확장자명
                String contentType = multipartFile.getContentType();

                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                } else { //확장자명이 jpeg, png 인 파일들만 받아서 처리
                    if (contentType.contains("image/jpeg")) extension = ".jpg";
                    else if (contentType.contains("image/png")) extension = ".png";
                    else {
                        log.info("사진이 아닌 파일입니다.");
                        break; //다른 확장자일 경우 처리 x
                    }
                }
                String storeImageName = createStoreImageName(extension);

                Image image = Image.builder()
                        .originImageName(multipartFile.getOriginalFilename())
                        .storeImageName(storeImageName)
                        .build();
                fileList.add(image);

                multipartFile.transferTo(new File(getFullPath(storeImageName)));
                log.info("로컬에 사진 저장");
            }
        }
        return fileList;
    }

    /**
     * 이미지 리스트 로컬에서 삭제
     * 부모가 지워질 때 cascade 에서 사용
     */
    public void deleteFile(List<Image> imageList) {
        for (Image image : imageList) {
            File file = new File(getFullPath(image.getStoreImageName()));
            boolean delete = file.delete();
            if (delete) log.info("로컬 파일 삭제 완료");
            else log.info("로컬 파일 삭제 실패");
        }
    }

    /**
     * 단일 이미지 삭제
     */
    public void deleteFileOne(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("이미지 파일이 없습니다."));
        imageRepository.deleteById(image.getId());
        File file = new File(getFullPath(image.getStoreImageName()));
        boolean delete = file.delete();
        if (delete) log.info("로컬 파일 삭제 완료");
        else log.info("로컬 파일 삭제 실패");
    }

    /**
     * 이름 중복을 피하기 위해 UUID.(파일타입) 로 storeFileName 생성
     */
    private String createStoreImageName(String extension) {
        String uuid = UUID.randomUUID().toString();
        String storeFileName = uuid + extension;
        return storeFileName;
    }

    /**
     * ex) C:/Users/(storeFileName).jpg 반환
     */
    public String getFullPath(String filename) {
        return fileDir + filename;
    }
}
