package com.uni.vetclinicapi.presentation.controller;

import com.uni.vetclinicapi.service.PetService;
import com.uni.vetclinicapi.service.UserService;
import com.uni.vetclinicapi.service.dto.ApiErrorResponseDTO;
import com.uni.vetclinicapi.service.dto.FullPetDTO;
import com.uni.vetclinicapi.service.dto.UserInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
@Tag(name = "admin", description = "The Admin API.")
public class AdminController {

    private final UserService userService;

    private final PetService petService;


    /**
     * Retrieves all users from database
     *
     * @return - response entity containing a list with all users with status code OK.
     */
    @Operation(summary = "Retrieves all users.", description = "Allows the admin to get all registered users.", tags = {"admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched all users.", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserInfoDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }


    /**
     * Deletes a user.
     *
     * @param userId - id for user to delete.
     * @return - response with status code OK if the user was deleted successfully or CONFLICT if the user with this id does not exist.
     */
    @Operation(summary = "Deletes a User.", description = "Deletes a User from database.", tags = {"admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes Pet from database.", content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
            @ApiResponse(responseCode = "404", description = "User does not exist in the database", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
    })
    @DeleteMapping("/users")
    public ResponseEntity<UserInfoDTO> deleteUser(
            @Parameter(description = "User id.")
            @RequestParam("userId") @NotNull UUID userId) {
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
    }


    /**
     * Fetches a user.
     *
     * @param userId - id for user to fetch.
     * @return - response with status code OK if the user was fetched successfully or CONFLICT if the user with this id does not exist.
     */
    @Operation(summary = "Fetches a User.", description = "Fetches a User from database.", tags = {"admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetches Pet from database.", content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
            @ApiResponse(responseCode = "404", description = "User does not exist in the database", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<UserInfoDTO> getUserById(
            @Parameter(description = "User id.")
            @PathVariable("userId") @NotNull UUID userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }


    /**
     * Delivers all the pets for given User.
     *
     * @return - response with status code OK if the pets were found successfully.
     */

    @Operation(summary = "Find all Pets for User.", description = "Delivers Pets from database.", tags = {"admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivers Pets for User from database.", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "User does not exist in the database", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
    })
    @GetMapping("/users/{userId}/pets")
    public ResponseEntity<List<FullPetDTO>> getAllPetsForUserById(
            @Parameter(description = "User id.")
            @PathVariable("userId") @NotNull UUID userId) {
        return new ResponseEntity<>(petService.findAllPetsForUserById(userId), HttpStatus.OK);
    }

}

