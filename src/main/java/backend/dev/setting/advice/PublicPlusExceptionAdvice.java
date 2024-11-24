package backend.dev.setting.advice;

import backend.dev.facility.exception.FacilityException;
import backend.dev.facility.exception.FacilityTaskException;
import backend.dev.setting.exception.ErrorResponse;
import backend.dev.setting.exception.PublicPlusCustomException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.processing.FilerException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class PublicPlusExceptionAdvice {
    @ExceptionHandler(PublicPlusCustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(PublicPlusCustomException e) {
        ErrorResponse response = ErrorResponse.of(e.getErrorCode().getHttpStatus(), e.getMessage());
        log.error("Error Message: {}", e.getErrorCode().getMessage());
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
    @ExceptionHandler(FacilityTaskException.class)
    public ResponseEntity<ErrorResponse> handleFilerException(FacilityTaskException e) {
        ErrorResponse response = ErrorResponse.of(HttpStatus.valueOf(e.getCode()), e.getMessage());
        log.error("Error Message: {}",e.getCode(), e.getMessage());
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
