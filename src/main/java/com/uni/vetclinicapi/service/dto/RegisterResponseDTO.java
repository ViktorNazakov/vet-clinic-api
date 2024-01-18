package com.uni.vetclinicapi.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class holds the information about a user that has successfully registered.
 */
@Schema(description = "This DTO holds information about a User that has successfully registered.", allowableValues = {"username", "email"})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterResponseDTO {

    @Schema(description = "The username of the user")
    private String username;

    @Schema(description = "The email of the user")
    private String email;

}