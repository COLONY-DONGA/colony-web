package colony.webproj.service;

import colony.webproj.entity.Image;
import colony.webproj.exception.CustomException;
import colony.webproj.exception.ErrorCode;
import colony.webproj.repository.imageRepository.ImageRepository;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final AmazonS3Client amazonS3Client;


    @Value("${file.dir}")
    private String fileDir;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

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

                if(contentType.contains("application/octet-stream")) {
                    break;
                }
                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                } else { //확장자명이 jpeg, png 인 파일들만 받아서 처리
                    if (contentType.contains("image/jpeg")) extension = ".jpg";
                    else if (contentType.contains("image/png")) extension = ".png";
                    else {
                        log.info("사진이 아닌 파일입니다.");
                        throw new CustomException(ErrorCode.IMAGE_NOT_SUPPORTED_EXTENSION);
                    }
                }
                String storeImageName = createStoreImageName(extension);

                Image image = Image.builder()
                        .originImageName(multipartFile.getOriginalFilename())
                        .storeImageName(storeImageName)
                        .build();

                InputStream inputStream = multipartFile.getInputStream();
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(multipartFile.getSize());
                metadata.setContentType(multipartFile.getContentType());

                PutObjectRequest request = new PutObjectRequest(bucket, storeImageName, inputStream, metadata);
                amazonS3Client.putObject(request);
                log.info("s3에 사진 저장");
                String s3Url = getImgPath(storeImageName);
                image.setS3Url(s3Url);

//                multipartFile.transferTo(new File(getFullPath(storeImageName)));
//                log.info("로컬에 사진 저장");

                //s3Url 까지 세팅 후 list 에 저장
                fileList.add(image);
            }
        }
        return fileList;
    }

    /**
     * S3 이미지 반환
     */
    public String getImgPath(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * 이미지 리스트 로컬에서 삭제
     * 부모가 지워질 때 cascade 에서 사용
     */
    public void deleteFile(List<Image> imageList) {
//        //로컬
//        for (Image image : imageList) {
//            File file = new File(getFullPath(image.getStoreImageName()));
//            boolean delete = file.delete();
//            if (delete) log.info("로컬 파일 삭제 완료");
//            else log.info("로컬 파일 삭제 실패");
//        }

        //s3
        for (Image image : imageList) {
            String storeImageName = image.getStoreImageName();
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, storeImageName);
            amazonS3Client.deleteObject(deleteObjectRequest);
        }
        log.info("S3 파일 삭제 완료");
    }

    public void deleteFileWithStoreImageName(List<String> imageList) {
        //s3
        for (String storeImageName : imageList) {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, storeImageName);
            amazonS3Client.deleteObject(deleteObjectRequest);
        }
        log.info("S3 파일 삭제 완료");
    }

    /**
     * 단일 이미지 삭제
     */
    public void deleteFileOne(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND));
        imageRepository.deleteById(image.getId());

//        //로컬
//        File file = new File(getFullPath(image.getStoreImageName()));
//        boolean delete = file.delete();
//        if (delete) log.info("로컬 파일 삭제 완료");
//        else log.info("로컬 파일 삭제 실패");

        //s3
        String storeImageName = image.getStoreImageName();
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, storeImageName);
        amazonS3Client.deleteObject(deleteObjectRequest);
        log.info("S3 파일 삭제 완료");
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

    public Boolean validateImageAndMember(Long memberId, Long imageId) {
        Image image = imageRepository.findImageWithPost(imageId)
                .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND));
        return (image.getPost().getMember().getId() == memberId) ? true : false;
    }
}
