package com.example.exception;

public class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException ( String message) {
        super(message);
    }

}
