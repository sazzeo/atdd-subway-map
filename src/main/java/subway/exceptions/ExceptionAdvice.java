package subway.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> illegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().build();
    }

}
