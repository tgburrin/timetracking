package net.tgburrin.timekeeping.exceptions;

public class InvalidApiTokenException  extends TimeKeepingException {
    public InvalidApiTokenException(String message) {super(message);}

    public InvalidApiTokenException(String message, Throwable err) {super(message, err);}
}
