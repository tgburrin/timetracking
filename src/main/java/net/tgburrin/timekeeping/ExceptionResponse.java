package net.tgburrin.timekeeping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@RestControllerAdvice
public class ExceptionResponse {
    Logger logger = LoggerFactory.getLogger(ExceptionResponse.class);

    public static ApiError returnErrorBody(Exception ex) {
        StackTraceElement ste = ex.getStackTrace()[0];
        return new ApiError(
                ex.getMessage(),
                ex.getClass().getSimpleName(),
                ste.getClassName(),
                ste.getMethodName()
        );
    }
    @ExceptionHandler(value = {
        InvalidRecordException.class,
    })
    protected ResponseEntity<ApiError> handleNotFoundInvalid(Exception ex, WebRequest request) {
        return new ResponseEntity<>(returnErrorBody(ex), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {
            InvalidDataException.class,
    })
    protected ResponseEntity<ApiError> handleInvalidData(Exception ex, WebRequest request) {
        return new ResponseEntity<>(returnErrorBody(ex), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {
            NoRecordFoundException.class,
    })
    protected ResponseEntity<ApiError> handleNoRecordReturned(Exception ex, WebRequest request) {
        StackTraceElement ste = ex.getStackTrace()[0];
        logger.info(ex.getClass().getSimpleName()+": "+ste.getClassName()+":"+ste.getMethodName());
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(value = {
            InvalidApiTokenException.class
    })
    protected ResponseEntity<ApiError> handleInvalidTokenReturned(Exception ex, WebRequest request) {
        return new ResponseEntity<>(returnErrorBody(ex), HttpStatus.FORBIDDEN);
    }
}
