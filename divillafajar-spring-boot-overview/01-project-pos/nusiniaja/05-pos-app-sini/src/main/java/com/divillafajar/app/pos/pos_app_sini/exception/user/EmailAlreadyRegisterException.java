package com.divillafajar.app.pos.pos_app_sini.exception.user;

import java.io.Serial;

public class EmailAlreadyRegisterException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 6850830898316004894L;

    public EmailAlreadyRegisterException(String message) {
        super(message);
    }

    public EmailAlreadyRegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailAlreadyRegisterException(Throwable cause) {
        super(cause);
    }
}
