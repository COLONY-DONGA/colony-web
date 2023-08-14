package colony.webproj.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."),
    POST_DELETE_WRONG_ACCESS(HttpStatus.UNAUTHORIZED, "본인의 게시글만 삭제할 수 있습니다."),
    POST_UPDATE_WRONG_ACCESS(HttpStatus.UNAUTHORIZED, "본인의 게시글만 수정할 수 있습니다."),
    POST_NO_REQUIRED_VALUE(HttpStatus.BAD_REQUEST, "게시글의 필수 값이 존재하지 않습니다."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "답변이 존재하지 않습니다."),
    ANSWER_DELETE_WRONG_ACCESS(HttpStatus.UNAUTHORIZED, "본인의 답변만 삭제할 수 있습니다."),
    ANSWER_UPDATE_WRONG_ACCESS(HttpStatus.UNAUTHORIZED, "본인의 답변만 수정할 수 있습니다."),
    ANSWER_NO_REQUIRED_VALUE(HttpStatus.BAD_REQUEST, "답변의 필수 값이 존재하지 않습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),
    COMMENT_PARENT_NOT_FOUND(HttpStatus.NOT_FOUND, "부모 댓글이 존재하지 않습니다."),
    COMMENT_DELETE_WRONG_ACCESS(HttpStatus.UNAUTHORIZED, "본인의 댓글만 삭제할 수 있습니다."),
    COMMENT_UPDATE_WRONG_ACCESS(HttpStatus.UNAUTHORIZED, "본인의 댓글만 수정할 수 있습니다."),
    COMMENT_NO_REQUIRED_VALUE(HttpStatus.BAD_REQUEST, "댓글의 필수 값이 존재하지 않습니다."),

    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "알람이 존재하지 않습니다."),
    ALARM_READ_PROCESS_WRONG_ACCESS(HttpStatus.UNAUTHORIZED, "알람을 받은 회원만 알람 읽음 처리를 할 수 있습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지가 존재하지 않습니다."),
    IMAGE_NOT_SUPPORTED_EXTENSION(HttpStatus.BAD_REQUEST, "지원하지 않는 이미지 형식입니다. jpg, png 형식으로 업로드 해주십시오."),
    IMAGE_DELETE_WRONG_ACCESS(HttpStatus.UNAUTHORIZED, "본인이 등록한 사진만 삭제할 수 있습니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요 누른 기록이 존재하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다.");
    private final HttpStatus status;
    private final String errorMessage;
}
