package net.tgburrin.timekeeping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class ExceptionResponse extends ResponseEntityExceptionHandler {
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
    // https://www.baeldung.com/spring-boot-bean-validation
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

    /*
    Handled in the filtering and never makes it to this layer
    @ExceptionHandler(value = {
            InvalidApiTokenException.class
    })
    protected ResponseEntity<ApiError> handleInvalidTokenReturned(Exception ex, WebRequest request) {
        return new ResponseEntity<>(returnErrorBody(ex), HttpStatus.FORBIDDEN);
    }
    */

    /*
    This cannot be caught at the moment or it squashes the underlying root cause
    bubbling up the underlying database message would be the ideal
    @ExceptionHandler(value = {DbActionExecutionException.class})
    protected ResponseEntity<ApiError> handleDatabaseException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(returnErrorBody(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    */
}
