package com.backend.starter.exception;

public class LoginException extends RuntimeException {
    public LoginException(String message){
        super(message);
    }
}
