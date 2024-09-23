package net.tgburrin.timekeeping.exceptions;

import java.util.List;

public class BadCredentialsException extends TimeKeepingException {

    public BadCredentialsException(List<String> messages) {
        super(messages);
    }

    public BadCredentialsException(String message) {
        super(message);
    }

    public BadCredentialsException(String message, Throwable err) {
        super(message, err);
    }
}
