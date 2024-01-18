package com.uni.vetclinicapi.presentation.controller;

import com.uni.vetclinicapi.service.MedicationService;
import com.uni.vetclinicapi.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/meds")
@RestController
@Tag(name = "meds", description = "The Medications API.")
public class MedicationController {

    private final MedicationService medicationService;

    /**
     * Creates a medication.
     *
     * @param medDTO - data that the created Medication must include.
     * @return - response with status code CREATED if the medication was created successfully or CONFLICT if the medication with this name already exists.
     */
    @Operation(summary = "Create new Medication.", description = "Creates new Medication and persists to database.", tags = {"meds"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medication created.", content = @Content(schema = @Schema(implementation = FullMedicationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Medication already exists.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<FullMedicationDTO> createMedication(
            @Parameter(
                    description = "Medication to create. Cannot be null or empty.",
                    required = true, schema = @Schema(implementation = MedicationDTO.class))
            @Valid @RequestBody MedicationDTO medDTO) {
        return new ResponseEntity<>(medicationService.create(medDTO), HttpStatus.CREATED);
    }

    /**
     * Retrieves all medications from database
     *
     * @return - response entity containing a list with all medications with status code OK.
     */
    @Operation(summary = "Retrieves all medications.", description = "Retrieves all medications.", tags = {"meds"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched all medications.", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
    })
    @GetMapping
    public ResponseEntity<List<FullMedicationDTO>> getAllMedications() {
        return new ResponseEntity<>(medicationService.getAllMedications(), HttpStatus.OK);
    }

    /**
     * Deletes a medication.
     *
     * @param medId - id for medication that is to be deleted.
     * @return - response with status code OK if the medication was deleted successfully or CONFLICT if the medication with this id does not exist.
     */

    @Operation(summary = "Deletes a Medication.", description = "Deletes Pet from database.", tags = {"meds"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes Medication from database.", content = @Content(schema = @Schema(implementation = FullMedicationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Medication does not exist in the database", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
    })

    @DeleteMapping
    public ResponseEntity<FullMedicationDTO> delete(
            @Parameter(description = "Med id.")
            @RequestParam("medId") @NotNull UUID medId) {
        return new ResponseEntity<>(medicationService.delete(medId), HttpStatus.OK);
    }




    @Operation(summary = "Update a medication's property", description = "Returns a FullMedicationDTO with updated property", tags = {"meds"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updates a given medication's property", content = @Content(schema = @Schema(implementation = FullMedicationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Medication does not exist in the database", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PatchMapping
    public ResponseEntity<FullMedicationDTO> updateMedicationProperty(
            @Parameter(description = "Medication id.")
            @RequestParam("medId") @NotNull UUID medId,
            @RequestBody MedicationDTO medDTO) {
        return new ResponseEntity<>(medicationService.updateMedicationProperty(medId, medDTO), HttpStatus.OK);
    }




    @Operation(summary = "Update a medication's quantity", description = "Returns a FullMedicationDTO with updated quantity", tags = {"meds"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updates a given medication's quantity", content = @Content(schema = @Schema(implementation = FullMedicationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Medication does not exist in the database", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PatchMapping("/{medId}")
    public ResponseEntity<FullMedicationDTO> updateMedicationQuantity(
            @Parameter(description = "Medication id.")
            @PathVariable("medId") @NotNull UUID medId,
            @RequestBody MedicationDTO medDTO) {
        return new ResponseEntity<>(medicationService.updateMedicationQuantity(medId, medDTO), HttpStatus.OK);
    }
}
