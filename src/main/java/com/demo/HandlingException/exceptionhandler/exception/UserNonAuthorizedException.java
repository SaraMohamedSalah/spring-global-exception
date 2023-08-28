package com.demo.HandlingException.exceptionhandler.exception;

public class UserNonAuthorizedException extends RuntimeException {
    public UserNonAuthorizedException(String message) {
        super(message);
    }

    public UserNonAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNonAuthorizedException(Throwable cause) {
        super(cause);
    }
}
