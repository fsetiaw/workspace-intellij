package com.divillafajar.app.pos.pos_app_sini.ws.exception;

public class GenericCustomErrorException extends RuntimeException {
    public GenericCustomErrorException(String message) {
        super(message);
    }

    public GenericCustomErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericCustomErrorException(Throwable cause) {
        super(cause);
    }
}
