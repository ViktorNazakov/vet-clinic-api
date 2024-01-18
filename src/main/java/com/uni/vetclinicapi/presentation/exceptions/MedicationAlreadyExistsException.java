package com.uni.vetclinicapi.presentation.exceptions;

/**
 * Thrown, when the visit date is taken or before the time of making the visit.
 */
public class MedicationAlreadyExistsException extends RuntimeException{
    public MedicationAlreadyExistsException(String message) {
        super(message);
    }

}
