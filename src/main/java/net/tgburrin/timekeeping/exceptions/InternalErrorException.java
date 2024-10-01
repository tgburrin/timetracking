package net.tgburrin.timekeeping.exceptions;

import java.util.List;

public class InternalErrorException extends TimeKeepingException {
    public InternalErrorException(List<String> messages) {
        super(messages);
    }

    public InternalErrorException(String message) {
        super(message);
    }

    public InternalErrorException(String message, Throwable err) {
        super(message, err);
    }
}
