package com.uni.vetclinicapi.presentation.exceptions;

/**
 * Thrown, when the username we are trying to register with already exists.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
