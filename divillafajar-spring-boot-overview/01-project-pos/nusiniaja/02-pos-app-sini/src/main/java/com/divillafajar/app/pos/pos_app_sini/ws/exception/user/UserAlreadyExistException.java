package com.divillafajar.app.pos.pos_app_sini.ws.exception.user;

import java.io.Serial;

public class UserAlreadyExistException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 6850830898316004894L;

    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
