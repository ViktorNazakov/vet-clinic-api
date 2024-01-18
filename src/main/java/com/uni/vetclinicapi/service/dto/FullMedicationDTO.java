package com.uni.vetclinicapi.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

/**
 * Provides Data transfer objects or DTOs for short.
 * Used to hide data that doesn't need to be exposed to the public and is lighter than the real Entity.
 */
@Schema(description = "Full Medication DTO is used for carrying information about the Medication Entity (ID included).", allowableValues = {"id", "registrationNumber", "color", "creationYear"})
@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Validated
public class FullMedicationDTO {

    @Schema(description = "Id of the Pet.", example = "480d68e4-3f13-48f0-a6ec-c94b4d0d7683")
    private UUID id;

    @Schema(description = "Name of the medication.")
    @NotNull(message = "Name should be specified.")
    @Size(min = 3, max = 15, message = "Number length should be of length from 5 to 15 symbols.")
    private String name;

    @Schema(description = "Type of the medication.")
    @NotNull(message = "Type should be specified.")
    @Size(min = 3, max = 15, message = "Color name should be of length from 3 to 15 symbols.")
    private String type;

    @Schema(description = "Quantity of the medication.")
    @NotNull(message = "Quantity should be specified.")
    private int quantity;

    @Schema(description = "Description of the medication")
    @NotNull(message = "Description should be specified")
    private String description;
}
