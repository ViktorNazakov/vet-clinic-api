package com.uni.vetclinicapi.presentation.controller;

import com.uni.vetclinicapi.service.PetService;
import com.uni.vetclinicapi.service.UserService;
import com.uni.vetclinicapi.service.VisitService;
import com.uni.vetclinicapi.service.dto.ApiErrorResponseDTO;
import com.uni.vetclinicapi.service.dto.FullPetDTO;
import com.uni.vetclinicapi.service.dto.FullVisitDTO;
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
@RequestMapping("/api/v1/users")
@RestController
@Tag(name = "users", description = "The Users API.")
public class UserController {

    private final UserService userService;

    private final PetService petService;

    private final VisitService visitService;

    /**
     * Returns currently logged-in user's info.
     *
     * @return - UserInfoDTO with status code OK(200).
     */
    @Operation(summary = "Get details for logged user", tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched details for logged user", content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))

    })
    @GetMapping
    public ResponseEntity<UserInfoDTO> getLoggedUserInfo(){
        return new ResponseEntity<>(userService.getLoggedUserInfo(), HttpStatus.OK);
    }

    /**
     * Delivers all the pets for current User.
     *
     * @return - response with status code OK if the pets were found successfully.
     */

    @Operation(summary = "Find all Pets for User.", description = "Delivers Pets from database.", tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivers Pets for User from database.", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
    })
    @GetMapping("/pets")
    public ResponseEntity<List<FullPetDTO>> getAllPetsForLoggedUser() {
        return new ResponseEntity<>(petService.findAllPetsForLoggedUser(), HttpStatus.OK);
    }

    /**
     * Deletes a pet.
     *
     * @param petId - id for pet that user wants to delete.
     * @return - response with status code OK if the pet was deleted successfully or CONFLICT if the pet with this id does not exist.
     */

    @Operation(summary = "Deletes a Pet.", description = "Deletes Pet from database.", tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes Pet from database.", content = @Content(schema = @Schema(implementation = FullPetDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pet does not exist in the database", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
    })

    @DeleteMapping("/pets")
    public ResponseEntity<FullPetDTO> deletePetForUser(
            @Parameter(description = "Pet id.")
            @RequestParam("petId") @NotNull UUID petId) {
        return new ResponseEntity<>(petService.deletePetFromUser(petId), HttpStatus.OK);
    }


    @Operation(summary = "Find all Visits for User.", description = "Delivers Visits from database.", tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivers Pets for User from database.", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/visits")
    public ResponseEntity<List<FullVisitDTO>> getAllVisitsForUser() {
        return new ResponseEntity<>(visitService.findAllVisitsForUser(),HttpStatus.OK);
    }

    @Operation(summary = "Update a pet's property", description = "Returns a FullPetDTO with updated property", tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updates a given pet's property", content = @Content(schema = @Schema(implementation = FullPetDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pet does not exist in the database", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PatchMapping("/pets")
    public ResponseEntity<FullPetDTO> updatePetProperty(
            @Parameter(description = "pet id.")
            @RequestParam("petId") @NotNull UUID petId,
            @RequestBody FullPetDTO petDTO) {
        return new ResponseEntity<>(petService.updatePetProperty(petId, petDTO), HttpStatus.OK);
    }

    @Operation(summary = "Update a user's property", description = "Returns a UserDTO with updated property", tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updates a given user's property", content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
            @ApiResponse(responseCode = "404", description = "User does not exist in the database", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PatchMapping
    public ResponseEntity<UserInfoDTO> updateUserProperty(
            @Parameter(description = "User id.")
            @RequestParam("userId") @NotNull UUID userId, @RequestBody UserInfoDTO userInfoDTO) {
        return new ResponseEntity<>(userService.updateUserProperty(userId, userInfoDTO), HttpStatus.OK);
    }


    /**
     * Retrieves all users with Vet role from database
     *
     * @return - response entity containing a list with all users with Vet role with status code OK.
     */
    @Operation(summary = "Retrieves all users with Vet role.", description = "Allows the user to get a list of vets.", tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched all users with role Vet.", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
    })
    @GetMapping("/vets")
    public ResponseEntity<List<UserInfoDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsersWithRoleVet(), HttpStatus.OK);
    }
}
