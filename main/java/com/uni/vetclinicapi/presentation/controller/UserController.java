package com.uni.vetclinicapi.presentation.controller;

import com.uni.vetclinicapi.service.PetService;
import com.uni.vetclinicapi.service.UserService;
import com.uni.vetclinicapi.service.dto.ApiErrorResponseDTO;
import com.uni.vetclinicapi.service.dto.FullPetDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
@Tag(name = "users", description = "The Users API.")
public class UserController {

    private final UserService userService;

    private final PetService petService;

    /**
     * Add a pet to the current logged user's profile.
     *
     * @param petId - the id of the pet.
     * @return - FullPetDTO with status code OK(200).
     */
    @Operation(summary = "Add pet to user profile.", description = "Add car to current logged user profile.", tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added car to user profile and returned Full Pet DTO.", content = @Content(schema = @Schema(implementation = FullPetDTO.class))),
            @ApiResponse(responseCode = "400", description = "Pet with such id doesn't exist, bad request.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Pet with such id is already owned by another user, bad request.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
    })
    @PutMapping("/pets")
    public ResponseEntity<FullPetDTO> addPetToUser(
            @Parameter(description = "Pet id.")
            @PathVariable("petId") @NotNull UUID petId){
        return new ResponseEntity<>(userService.addPetToUser(petId), HttpStatus.OK);
    }

    /**
     * Delivers all the pets for current User.
     *
     * @return - response with status code OK if the pets were found successfully.
     */

    @Operation(summary = "Find all Pets for User.", description = "Delivers Pets from database.", tags = {"pets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivers Pets for User from database.", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
    })
    @GetMapping("/pets")
    public ResponseEntity<List<FullPetDTO>> getAllPetsForUser() {
        return new ResponseEntity<>(petService.findAllPetsForUser(), HttpStatus.OK);
    }

    /**
     * Deletes a pet.
     *
     * @param petId - id for pet that user wants to delete.
     * @return - response with status code OK if the pet was deleted successfully or CONFLICT if the pet with this id does not exist.
     */

    @Operation(summary = "Deletes a Pet.", description = "Deletes Pet from database.", tags = {"pets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes Pet from database.", content = @Content(schema = @Schema(implementation = FullPetDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pet does not exist in the database", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
    })

    @DeleteMapping("/pets")
    public ResponseEntity<FullPetDTO> deletePetForUser(
            @Parameter(description = "Pet id.")
            @PathVariable("petId") @NotNull UUID petId) {
        return new ResponseEntity<>(petService.deletePetFromUser(petId), HttpStatus.OK);
    }
}
