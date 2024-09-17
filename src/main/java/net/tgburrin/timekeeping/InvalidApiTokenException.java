package net.tgburrin.timekeeping;

public class InvalidApiTokenException  extends RuntimeException {
    public InvalidApiTokenException(String message) {super(message);}

    public InvalidApiTokenException(String message, Throwable err) {super(message, err);}
}
