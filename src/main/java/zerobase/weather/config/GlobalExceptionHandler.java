package zerobase.weather.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zerobase.weather.config.dto.ExceptionDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String ERROR_CODE = "9999";
    private static final String ERROR_MESSAGE = "에러가 발생하였습니다.";

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleAllException() {
        System.out.println("error from GlobalExceptionHandler");
        return ResponseEntity.internalServerError()
            .body(new ExceptionDto(ERROR_CODE, ERROR_MESSAGE));
    }

}
