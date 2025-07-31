package com.divillafajar.app.pos.pos_app_sini.ws.exception;

import java.io.Serial;

public class GenericCustomErrorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5703954135216553317L;

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
