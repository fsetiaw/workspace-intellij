package com.divillafajar.app.pos.pos_app_sini.exception.user;

import java.io.Serial;

public class CreateUserException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 862161214476054758L;

    public CreateUserException(String message) {
        super(message);
    }

    public CreateUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateUserException(Throwable cause) {
        super(cause);
    }
}
