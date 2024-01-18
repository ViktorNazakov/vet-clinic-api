package com.uni.vetclinicapi.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * This class holds the information about a user that has logged in.
 */
@Schema(description = "This DTO holds information about a User that has successfully registered.", allowableValues = {"username", "email", "password", "fName","lName", "phoneNumber"})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoDTO {

    @Schema(description = "The id of the user")
    private UUID userId;

    @Schema(description = "The username of the user")
    private String username;

    @Schema(description = "The email of the user")
    private String email;

    @Schema(description = "The first name of the user")
    private String fName;

    @Schema(description = "The last name of the user")
    private String lName;

    @Schema(description = "The phone number of the user")
    private String phoneNumber;

    @Schema(description = "The type of vet")
    private String vetType;

    @Schema(name = "role", description = "The user's role")
    private String role;
}