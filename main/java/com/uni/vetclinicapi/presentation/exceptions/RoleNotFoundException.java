package com.uni.vetclinicapi.presentation.exceptions;
/**
 * Thrown if a role that we want to access doesn't exist.
 */

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String message){
        super(message);
    }
}
