package com.uni.vetclinicapi.presentation.exceptions;

/**
 * Thrown, when the username we are trying to register with already exists.
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
