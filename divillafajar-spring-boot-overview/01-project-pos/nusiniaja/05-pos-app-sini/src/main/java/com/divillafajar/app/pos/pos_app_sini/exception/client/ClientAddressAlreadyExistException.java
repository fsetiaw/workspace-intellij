package com.divillafajar.app.pos.pos_app_sini.exception.client;

import java.io.Serial;

public class ClientAddressAlreadyExistException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 60871232378716937L;

    public ClientAddressAlreadyExistException(String message) {
        super(message);
    }

    public ClientAddressAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientAddressAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
