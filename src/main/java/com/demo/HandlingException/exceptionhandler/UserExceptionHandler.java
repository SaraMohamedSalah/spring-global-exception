package com.demo.HandlingException.exceptionhandler;

import com.demo.HandlingException.exceptionhandler.exception.UserException;
import com.demo.HandlingException.exceptionhandler.exception.UserNonAuthorizedException;
import com.demo.HandlingException.models.UserErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {
    public UserExceptionHandler() {
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserException exc) {
        UserErrorResponse error = new UserErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserNonAuthorizedException exc) {
        UserErrorResponse error = new UserErrorResponse();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
    }
}
