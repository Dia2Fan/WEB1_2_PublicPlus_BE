package backend.dev.setting.advice;

import backend.dev.setting.exception.ErrorResponse;
import backend.dev.setting.exception.PublicPlusCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class PublicPlusExceptionAdvice {
    @ExceptionHandler(PublicPlusCustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(PublicPlusCustomException e) {
        ErrorResponse response = ErrorResponse.of(e.getErrorCode().getHttpStatus(), e.getMessage());
        log.error("Error Message: {}", e.getErrorCode().getMessage());
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        e.printStackTrace(); // 로깅 필요 시 유지
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
    }
}
