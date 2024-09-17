package net.tgburrin.timekeeping;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/*
https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
 */
public class ApiError {
    @JsonProperty("timestamp")
    private final Instant currentTime;
    private final String message;
    @JsonProperty("exception_name")
    private final String exceptionName;

    @JsonProperty("exception_class")
    private final String exceptinoClass;
    @JsonProperty("exception_method")
    private final String exceptionMethod;

    public ApiError(String m, String exName, String exClass, String exMethod) {
        currentTime = Instant.now();
        exceptionName = exName;
        exceptinoClass = exClass;
        exceptionMethod = exMethod;
        message = m;
    }

    public String getMessage() {
        return message;
    }

    public String getCurrentTime() {
        return currentTime.toString();
    }
}