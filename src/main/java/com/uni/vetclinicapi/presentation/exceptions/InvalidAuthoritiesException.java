package com.uni.vetclinicapi.presentation.exceptions;

/**
 * Thrown, when the authorities of the user are not the required ones.
 */
public class InvalidAuthoritiesException extends RuntimeException {
    public InvalidAuthoritiesException(String message) {
        super(message);
    }
}
