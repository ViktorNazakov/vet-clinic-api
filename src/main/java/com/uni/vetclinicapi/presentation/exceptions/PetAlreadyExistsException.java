package com.uni.vetclinicapi.presentation.exceptions;

/**
 * Thrown, when the username we are trying to register with already exists.
 */
public class PetAlreadyExistsException extends RuntimeException{
    public PetAlreadyExistsException(String message) {
        super(message);
    }

}
