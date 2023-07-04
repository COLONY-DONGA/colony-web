package colony.webproj.socket;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmDto {
    private Long alarmId;

    private String sendId;
    private String receiveId;
    private String postTitle;
    private String message;
    private String code;
}
