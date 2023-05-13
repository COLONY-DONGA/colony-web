package colony.webproj.service;

import colony.webproj.entity.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

@Component
public class FileHandler {

    @Value("${file.dir}")
    String fileDir;

    public List<Image> uploadFile(List<MultipartFile> multipartFiles) throws IOException {
        List<Image> fileList = new ArrayList<>();

        /* 비었는지 체크 */
        if(!CollectionUtils.isEmpty(multipartFiles)) {
            for(MultipartFile multipartFile : multipartFiles) {
                String originalFileExtension; //확장자명
                String contentType = multipartFile.getContentType();

                if(ObjectUtils.isEmpty(contentType)) {
                    break;
                }
                else { //확장자명이 jpeg, png 인 파일들만 받아서 처리
                    if(contentType.contains("image/jpeg")) originalFileExtension = ".jpg";
                    else if (contentType.contains("image/png")) originalFileExtension = ".png";
                    else break; //다른 확장자일 경우 처리 x
                }

                String storeImageName = createStoreImageName();

                Image image = Image.builder()
                        .originImageName(multipartFile.getOriginalFilename())
                        .storeImageName(storeImageName)
                        .build();
                fileList.add(image);

                multipartFile.transferTo(new File(getFullPath(storeImageName)));
            }
        }
        return fileList;
    }

    /**
     * 이름 중복을 피하기 위해 UUID.(파일타입) 로 storeFileName 생성
     */
    private String createStoreImageName() {
        String uuid = UUID.randomUUID().toString();
        String storeFileName = uuid;
        return storeFileName;
    }

    /**
     * ex) C:/Users/(storeFileName).jpg 반환
     */
    public String getFullPath(String filename) {
        return fileDir + filename;
    }
}
