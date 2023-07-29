package colony.webproj.sse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class NotificationContent {

    private static final int Max_LENGTH = 50;

    @Column(nullable = false,length = Max_LENGTH)
    private String content;

//    public NotificationContent(String content){
////        if(isNotValidNotificationContent(content)){
////            throw new CustomException(ErrorCode.NOT_VALIDCONTENT);
////        }
//        this.content = content;
//    }
//
//    private boolean isNotValidNotificationContent(String content) {
//        return Objects.isNull(content) || content.length() > Max_LENGTH
//        || content.isEmpty();
//
//    }
}
