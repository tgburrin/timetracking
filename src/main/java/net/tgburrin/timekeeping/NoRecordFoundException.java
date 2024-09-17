package net.tgburrin.timekeeping;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class NoRecordFoundException extends RuntimeException {
    public NoRecordFoundException (String message) {
        super(message);
    }

    public NoRecordFoundException (String message, Throwable err) {
        super(message, err);
    }
}
