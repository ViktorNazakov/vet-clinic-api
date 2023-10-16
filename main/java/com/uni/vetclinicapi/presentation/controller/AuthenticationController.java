package com.uni.vetclinicapi.presentation.controller;

import com.uni.vetclinicapi.service.AuthenticationService;
import com.uni.vetclinicapi.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Processes requests regarding "/auth" endpoint coming to the api, and returns responses based on them.
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
@Tag(name = "auth", description = "The Authentication API.")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Tries to sign-up a given valid user given by the request body.
     *
     * @param createUserDto - contains the info needed to register a user account.
     * @return - response entity containing the status code CREATED.
     */
    @Operation(summary = "Register account.", description = "Allows the user to register an account.", tags = {"authentication"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully signed up an account.", content = @Content(schema = @Schema(implementation = RegisterRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "The request body is not correct.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "The username already exists.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO createUserDto) {
        return new ResponseEntity<>(authenticationService.register(createUserDto), HttpStatus.CREATED);
    }

    /**
     * Tries to sign-in a given valid user given by the request body.
     *
     * @param loginRequestDTO - contains the info needed to login a user account.
     * @return - JWT token needed to authenticate the user.
     */
    @Operation(summary = "Login into the application.", description = "Allows the user to login to existing account.", tags = {"authentication"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in with the account.", content = @Content(schema = @Schema(implementation = LoginRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "The request body is not correct.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Incorrect username/password or Account doesn't exist.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(authenticationService.login(loginRequestDTO), HttpStatus.OK);
    }
}

