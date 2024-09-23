package net.tgburrin.timekeeping.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class NoRecordFoundException extends TimeKeepingException {
    public NoRecordFoundException (String message) {
        super(message);
    }

    public NoRecordFoundException (String message, Throwable err) {
        super(message, err);
    }
}
