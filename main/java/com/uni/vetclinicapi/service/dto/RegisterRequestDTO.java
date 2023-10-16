package com.uni.vetclinicapi.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class holds the information about a user that's trying to register.
 */
@Schema(description = "This DTO holds information about a User that's trying to register.", allowableValues = {"username", "email", "password"})
@AllArgsConstructor
@Data
public class RegisterRequestDTO {

    @Schema(description = "The username of the client")
    @NotBlank(message = "Username is required!")
    @Size(max = 20, min = 5)
    private String username;

    @Schema(description = "The email of the client")
    @NotBlank(message = "Email is required!")
    @Email
    @Size(max = 30, min = 10)
    private String email;

    @Schema(description = "The password of the client")
    @NotBlank(message = "Password is required!")
    @Size(max = 20, min = 5)
    private String password;
}
