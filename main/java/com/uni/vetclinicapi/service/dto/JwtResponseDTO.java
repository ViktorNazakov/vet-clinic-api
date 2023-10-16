package com.uni.vetclinicapi.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class contains the jwt token.
 */
@Schema(description = "This DTO holds the JWT Token throughout the requests.", allowableValues = {"token"})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponseDTO {
    @Schema(name = "token", description = "The JWT token")
    private String token;
}
