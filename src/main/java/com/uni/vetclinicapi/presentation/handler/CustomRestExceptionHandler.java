package com.uni.vetclinicapi.presentation.handler;

import com.uni.vetclinicapi.presentation.exceptions.*;
import com.uni.vetclinicapi.service.dto.ApiErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all possible exceptions, produced during the runtime of the api.
 */
@Slf4j
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles errors, regarding @Valid annotation.
     *
     * @param ex         - contains errors about the problem fields that don't pass the validation.
     * @param headers    - not being used, required by the overridden method.
     * @param status     - HTTP STATUS CODE of the error.
     * @param webRequest - not being used, required by the overridden method.
     * @return - contains the information about the error that has occurred - time of occurrence of error, http status code of the error, body with description about the errors.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest webRequest) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(objectError -> errors.add(objectError.getObjectName() + ": " + objectError.getDefaultMessage()));

        ApiErrorResponseDTO apiErrorResponseDTO = new ApiErrorResponseDTO((HttpStatus) status, ex.getLocalizedMessage(), errors);

        return new ResponseEntity<>(apiErrorResponseDTO, headers, status);
    }

    /**
     * Returns exception message with status code conflict, when we try to create a User with username, which already exists in some User Entity in database.
     *
     * @param e - the exception thrown.
     * @return - response, containing the exception message and appropriate status code.
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleUsernameAlreadyExists(UsernameAlreadyExistsException e) {
        String exceptionMessage = e.getLocalizedMessage();
        log.warn(exceptionMessage);
        return new ResponseEntity<>(new ApiErrorResponseDTO(HttpStatus.CONFLICT, exceptionMessage, List.of(e.getMessage())), HttpStatus.CONFLICT);
    }

    /**
     * Returns exception message with status code conflict, when we try to create a User with username, which already exists in some User Entity in database.
     *
     * @param e - the exception thrown.
     * @return - response, containing the exception message and appropriate status code.
     */
    @ExceptionHandler(PetAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponseDTO> handlePetAlreadyExists(PetAlreadyExistsException e) {
        String exceptionMessage = e.getLocalizedMessage();
        log.warn(exceptionMessage);
        return new ResponseEntity<>(new ApiErrorResponseDTO(HttpStatus.CONFLICT, exceptionMessage, List.of(e.getMessage())), HttpStatus.CONFLICT);
    }

    /**
     * Returns exception message with status code conflict, when we try to create a User with username, which already exists in some User Entity in database.
     *
     * @param e - the exception thrown.
     * @return - response, containing the exception message and appropriate status code.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleEmailAlreadyExists(EmailAlreadyExistsException e) {
        String exceptionMessage = e.getLocalizedMessage();
        log.warn(exceptionMessage);
        return new ResponseEntity<>(new ApiErrorResponseDTO(HttpStatus.CONFLICT, exceptionMessage, List.of(e.getMessage())), HttpStatus.CONFLICT);
    }

    /**
     * Returns exception message with status code not found, when we try to delete a pet with id, which does not exist in database.
     *
     * @param e - the exception thrown.
     * @return - response, containing the exception message and appropriate status code.
     */
    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDTO> handlePetNotFound(PetNotFoundException e) {
        String exceptionMessage = e.getLocalizedMessage();
        log.warn(exceptionMessage);
        return new ResponseEntity<>(new ApiErrorResponseDTO(HttpStatus.NOT_FOUND, exceptionMessage, List.of(e.getMessage())), HttpStatus.NOT_FOUND);
    }

    /**
     * Returns exception message with status code not found, when we try to get a vet with id, which does not exist in database.
     *
     * @param e - the exception thrown.
     * @return - response, containing the exception message and appropriate status code.
     */
    @ExceptionHandler(VetNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleVetNotFound(VetNotFoundException e) {
        String exceptionMessage = e.getLocalizedMessage();
        log.warn(exceptionMessage);
        return new ResponseEntity<>(new ApiErrorResponseDTO(HttpStatus.NOT_FOUND, exceptionMessage, List.of(e.getMessage())), HttpStatus.NOT_FOUND);
    }

    /**
     * Returns exception message with status code bad request, when we try to create a visit with date and hour, which already exists in database or is before the time of creating.
     *
     * @param e - the exception thrown.
     * @return - response, containing the exception message and appropriate status code.
     */
    @ExceptionHandler(InvalidVisitDateException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleInvalidVisitDate(InvalidVisitDateException e) {
        String exceptionMessage = e.getLocalizedMessage();
        log.warn(exceptionMessage);
        return new ResponseEntity<>(new ApiErrorResponseDTO(HttpStatus.BAD_REQUEST, exceptionMessage, List.of(e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns exception message with status code bad request, when we try to delete a user with id, which does not exist in database.
     *
     * @param e - the exception thrown.
     * @return - response, containing the exception message and appropriate status code.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleUserNotFound(UserNotFoundException e) {
        String exceptionMessage = e.getLocalizedMessage();
        log.warn(exceptionMessage);
        return new ResponseEntity<>(new ApiErrorResponseDTO(HttpStatus.BAD_REQUEST, exceptionMessage, List.of(e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns exception message with status code unauthorized, when we try to update a different user from currently logged or currently logged user does not have ADMIN authorities to update other users.
     *
     * @param e - the exception thrown.
     * @return - response, containing the exception message and appropriate status code.
     */
    @ExceptionHandler(InvalidAuthoritiesException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleInvalidAuthorities(InvalidAuthoritiesException e) {
        String exceptionMessage = e.getLocalizedMessage();
        log.warn(exceptionMessage);
        return new ResponseEntity<>(new ApiErrorResponseDTO(HttpStatus.UNAUTHORIZED, exceptionMessage, List.of(e.getMessage())), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Returns exception message with status code conflict, when we try to create a medication with name which already exists.
     *
     * @param e - the exception thrown.
     * @return - response, containing the exception message and appropriate status code.
     */
    @ExceptionHandler(MedicationAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleMedicationAlreadyExists(MedicationAlreadyExistsException e) {
        String exceptionMessage = e.getLocalizedMessage();
        log.warn(exceptionMessage);
        return new ResponseEntity<>(new ApiErrorResponseDTO(HttpStatus.CONFLICT, exceptionMessage, List.of(e.getMessage())), HttpStatus.CONFLICT);
    }

    /**
     * Returns exception message with status code bad request, when we try to fetch a medication with id which does not exist.
     *
     * @param e - the exception thrown.
     * @return - response, containing the exception message and appropriate status code.
     */
    @ExceptionHandler(MedicationNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleMedicationNotFound(MedicationNotFoundException e) {
        String exceptionMessage = e.getLocalizedMessage();
        log.warn(exceptionMessage);
        return new ResponseEntity<>(new ApiErrorResponseDTO(HttpStatus.BAD_REQUEST, exceptionMessage, List.of(e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns exception message with status code conflict, when quantity of a medication is insufficient.
     *
     * @param e - the exception thrown.
     * @return - response, containing the exception message and appropriate status code.
     */
    @ExceptionHandler(MedicationInsufficientQuantity.class)
    public ResponseEntity<ApiErrorResponseDTO> handleMedicationInsufficientQuantity(MedicationInsufficientQuantity e) {
        String exceptionMessage = e.getLocalizedMessage();
        log.warn(exceptionMessage);
        return new ResponseEntity<>(new ApiErrorResponseDTO(HttpStatus.CONFLICT, exceptionMessage, List.of(e.getMessage())), HttpStatus.CONFLICT);
    }
}
