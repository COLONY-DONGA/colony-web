package colony.webproj.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomException extends RuntimeException {
    private String status;
    private String errorMessage;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        setStatus(errorCode.getStatus().toString());
        setErrorMessage(errorCode.getErrorMessage());
    }
}
