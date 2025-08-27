package com.divillafajar.app.pos.pos_app_sini.exception.user;

import com.divillafajar.app.pos.pos_app_sini.exception.GenericCustomErrorException;
import com.divillafajar.app.pos.pos_app_sini.exception.model.GenericErrorResponse;
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

    @ExceptionHandler
    public ResponseEntity<GenericErrorResponse> handleException(UserAlreadyExistException exc) {
        GenericErrorResponse err = new GenericErrorResponse();
        err.setStatus(HttpStatus.CONFLICT.value());
        err.setMessage(exc.getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return  new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }
}
