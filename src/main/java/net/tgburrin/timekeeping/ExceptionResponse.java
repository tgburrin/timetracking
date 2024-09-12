package net.tgburrin.timekeeping;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResponse {
    @ExceptionHandler(InvalidDataException.class)
    public InvalidDataException handleInvalidDataException(InvalidDataException ex) {
        return ex;
    }
}
