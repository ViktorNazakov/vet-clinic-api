package com.uni.vetclinicapi.presentation.exceptions;
/**
 * Thrown, when the username we are trying to register with already exists.
 */
public class PetNotFoundException extends RuntimeException{
    public PetNotFoundException(String message) {
        super(message);
    }

}
