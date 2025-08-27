package com.divillafajar.app.pos.pos_app_sini.exception.client;

import java.io.Serial;

public class ClientAlreadyExistException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 60871232378716937L;

    public ClientAlreadyExistException(String message) {
        super(message);
    }

    public ClientAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
