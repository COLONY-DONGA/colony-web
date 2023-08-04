package colony.webproj.dto;

import colony.webproj.entity.Image;
import lombok.Getter;

@Getter
public class ImageDto {
    private Long imageId;
    private String storeImageName;   // 저장된 이름
    private String originImageName;  // 사용자가 올린 파일 이름
    private String s3Url;

    public ImageDto(Image image) {
        this.imageId = image.getId();
        this.storeImageName = image.getStoreImageName();
        this.originImageName = image.getOriginImageName();
        this.s3Url = image.getS3Url();
    }
}
