package com.uni.vetclinicapi.presentation.exceptions;
/**
 * Thrown, when the vet we are trying to get does not exist.
 */
public class VetNotFoundException extends RuntimeException{
    public VetNotFoundException(String message) {
        super(message);
    }

}
