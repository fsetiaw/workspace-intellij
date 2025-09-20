package com.divillafajar.app.pos.pos_app_sini.exception;

import java.io.Serial;

public class DuplicationErrorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5703954135216553317L;

    public DuplicationErrorException(String message) {
        super(message);
    }

    public DuplicationErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicationErrorException(Throwable cause) {
        super(cause);
    }
}
