package com.francis.Security.exceptions;

public class PasswordMissMatchException extends RuntimeException {
    public PasswordMissMatchException(String message) {
        super(message);
    }
}
