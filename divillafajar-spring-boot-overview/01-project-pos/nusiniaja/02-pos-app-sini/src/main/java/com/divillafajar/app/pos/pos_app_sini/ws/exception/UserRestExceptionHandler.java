package com.divillafajar.app.pos.pos_app_sini.ws.exception;

import com.divillafajar.app.pos.pos_app_sini.ws.model.exception.GenericErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserRestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<GenericErrorResponse> handleException(GenericCustomErrorException exc) {
        GenericErrorResponse err = new GenericErrorResponse();
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setMessage(exc.getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return  new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
}
