package com.uni.vetclinicapi.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * This class holds the username and password of a user that's trying to login.
 */
@Schema(description = "This DTO holds information about a User that's trying to login into the system.", allowableValues = {"username", "password"})
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequestDTO {

    @Schema(description = "The username of the user.")
    @NotBlank
    @Size(max = 20, min = 5)
    private String username;

    @Schema(description = "The password of the user.")
    @NotBlank
    @Size(max = 20, min = 5)
    private String password;
}

