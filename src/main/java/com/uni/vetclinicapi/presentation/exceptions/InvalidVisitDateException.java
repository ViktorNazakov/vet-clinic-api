package com.uni.vetclinicapi.presentation.exceptions;

/**
 * Thrown, when the visit date is taken or before the time of making the visit.
 */
public class InvalidVisitDateException extends RuntimeException {
    public InvalidVisitDateException(String message) {
        super(message);
    }
}
