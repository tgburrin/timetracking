package net.tgburrin.timekeeping;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

/*
https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
 */
public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}