package net.tgburrin.timekeeping;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidDataException extends TimeKeepingException {
    public InvalidDataException(List<String> messages) {
        super(messages);
    }

    public InvalidDataException(String message) {
        super(message);
    }
}
