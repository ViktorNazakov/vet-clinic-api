package com.uni.vetclinicapi.presentation.exceptions;

/**
 * Thrown, when the username we are trying to register with already exists.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}