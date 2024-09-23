package net.tgburrin.timekeeping;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidRecordException extends TimeKeepingException {
    public InvalidRecordException (String message) {
        super(message);
    }
    public InvalidRecordException(String message, Throwable err) {
        super(message, err);
    }
}
