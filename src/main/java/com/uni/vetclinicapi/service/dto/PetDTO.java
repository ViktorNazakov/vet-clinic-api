package com.uni.vetclinicapi.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;



/**
 * Provides Data transfer objects or DTOs for short.
 * Used to hide data that doesn't need to be exposed to the public and is lighter than the real Entity.
 */
@Schema(description = "Pet DTO is used for carrying information about the Pet Entity.", allowableValues = {"name", "specie", "breed"})
@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Validated
public class PetDTO {

    @Schema(description = "Name of the pet.",
            example = "Charlie")
    @NotNull(message = "Name should be specified.")
    @Size(min = 3, max = 15, message = "Number length should be of length from 5 to 15 symbols.")
    private String name;

    @Schema(description = "Specie of the pet.",
            example = "Dog")
    @NotNull(message = "Specie should be specified.")
    @Size(min = 3, max = 10, message = "Color name should be of length from 3 to 10 symbols.")
    private String specie;

    @Schema(description = "Breed of the specie.",
            example = "German shepard")
    @NotNull(message = "Breed should be specified.")
    private String breed;

}
