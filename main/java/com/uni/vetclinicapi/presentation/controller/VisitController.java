package com.uni.vetclinicapi.presentation.controller;

import com.uni.vetclinicapi.service.VisitService;
import com.uni.vetclinicapi.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Processes requests regarding "/visits" endpoint coming to the api, and returns responses based on them.
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/visits")
@RestController
@Tag(name = "visits", description = "The Visit API.")
public class VisitController {

    private final VisitService visitService;

    /**
     * Creates a visit.
     *
     * @param visitDTO - data that the created Pet must include.
     * @return - response with status code CREATED if the car was created successfully or CONFLICT if the pet or vet does not exist.
     */
    @Operation(summary = "Create new Pet.", description = "Creates new Pet and persists to database.", tags = {"pets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Visit created.", content = @Content(schema = @Schema(implementation = FullVisitDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Vet or Pet does not exist.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<FullVisitDTO> addVisit(
            @Parameter(
                    description = "Visit to create. Cannot be null or empty.",
                    required = true, schema = @Schema(implementation = VisitDTO.class))
            @Valid @RequestBody VisitDTO visitDTO) {
        return new ResponseEntity<>(visitService.addVisit(visitDTO), HttpStatus.CREATED);
    }
}
