package com.uni.vetclinicapi.service.dto;

import com.uni.vetclinicapi.persistance.entity.User;
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
@Schema(description = "Full Pet DTO is used for carrying information about the Pet Entity (ID included).", allowableValues = {"id", "name", "specie", "breed","user"})
@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Validated
public class FullPetDTO {

    @Schema(description = "Id of the Pet.", example = "480d68e4-3f13-48f0-a6ec-c94b4d0d7683")
    private UUID id;

    @Schema(description = "Name of the pet.",
            example = "Charlie")
    @NotNull(message = "Name should be specified.")
    @Size(min = 5, max = 15, message = "Number length should be of length from 5 to 15 symbols.")
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

    @Schema(description = "User that owns the pet.",
            example = "Viktor")
    private User user;
}
