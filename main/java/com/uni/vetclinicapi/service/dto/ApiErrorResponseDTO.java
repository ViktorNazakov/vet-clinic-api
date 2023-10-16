package com.uni.vetclinicapi.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * This class holds the information about errors that happen during the runtime of the application.
 */
@Schema(description = "This DTO holds the information about errors that happen during the runtime of the application.", allowableValues = {"status", "message", "errors"})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiErrorResponseDTO {

    @Schema(description = "The http status code of the response.")
    private HttpStatus status;

    @Schema(description = "The message of the error/exception.")
    private String message;

    @Schema(description = "The list of errors that occurred.")
    private List<String> errors;
}