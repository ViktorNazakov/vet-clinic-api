package com.uni.vetclinicapi.presentation.exceptions;

/**
 * Thrown, when the medication we are trying to fetch does not exist.
 */
public class MedicationNotFoundException extends RuntimeException {
    public MedicationNotFoundException(String message) {
        super(message);
    }
}