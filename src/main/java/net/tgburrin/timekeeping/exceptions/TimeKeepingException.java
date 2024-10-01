package net.tgburrin.timekeeping.exceptions;

import java.util.ArrayList;
import java.util.List;

abstract class TimeKeepingException extends RuntimeException  {
    private List<String> messages;

    public TimeKeepingException(List<String> messages) {
        super(messages.get(0));
        this.messages = messages;
    }

    public TimeKeepingException(String message) {
        super(message);
        messages = new ArrayList<>() {{ add(message); }};
    }

    public TimeKeepingException (String message, Throwable err) {
        super(message, err);
        messages = new ArrayList<>() {{ add(message); }};
    }
    public List<String> getMessages() {
        return messages;
    }
}
