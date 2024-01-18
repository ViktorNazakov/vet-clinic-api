package com.uni.vetclinicapi.service;

import com.uni.vetclinicapi.persistance.entity.Pet;
import com.uni.vetclinicapi.persistance.entity.User;
import com.uni.vetclinicapi.persistance.repository.PetRepository;
import com.uni.vetclinicapi.persistance.repository.UserRepository;
import com.uni.vetclinicapi.presentation.exceptions.PetAlreadyExistsException;
import com.uni.vetclinicapi.presentation.exceptions.PetNotFoundException;
import com.uni.vetclinicapi.presentation.exceptions.UserNotFoundException;
import com.uni.vetclinicapi.service.dto.FullPetDTO;
import com.uni.vetclinicapi.service.dto.PetDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Provides the necessary methods regarding CRUD operations with Pet Entities.
 * Uses the PetRepository as a connection with the database.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PetService {

    private final PetRepository petRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    /**
     * Checks if a Pet with name and owner like the one of PetDTO exists.
     * If it doesn't, Pet entity is created.
     * Else throws Pet already exists exception.
     *
     * @param petDTO - Pet data coming from request.
     * @return - FullPetDTO object, containing all the information for the Pet Entity.
     */

    public FullPetDTO create(PetDTO petDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean exists = petRepository.findAllByUser(user).stream().anyMatch(pet -> pet.getName().equals(petDTO.getName()));
        if (exists) {
            log.warn("Attempted to create a Pet with name: {} for User with id: {}, which already exists.", petDTO.getName(),user.getId());
            throw new PetAlreadyExistsException(String.format("Pet with the same name: %s and owner: %s already existsw!", petDTO.getName(),user.getUsername()));
        }

        Pet pet = modelMapper.map(petDTO, Pet.class);
        pet.setUser(user);
        Pet persistedPet = petRepository.save(pet);
        log.info("Pet with details : {}, was created!", pet);
        return modelMapper.map(persistedPet, FullPetDTO.class);
    }



    public List<FullPetDTO> findAllPetsForUserById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Attempted to fetch a User with id: {} , which does not exist.", userId);
            throw new UserNotFoundException(String.format("User with id: %s does not exist!", userId));
        });
        List<FullPetDTO> fullPetDTOList = petRepository.findAllByUser(user)
                .stream()
                .map(pet -> modelMapper.map(pet, FullPetDTO.class)).toList();
        if (fullPetDTOList.isEmpty()) {
            log.info("There are no Pets present in database!");
        } else {
            log.info("{} Pets for user with id : {}, have been fetched from database.", fullPetDTOList.size(), user.getId());
        }
        return fullPetDTOList;
    }

    public List<FullPetDTO> findAllPetsForLoggedUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<FullPetDTO> fullPetDTOList = petRepository.findAllByUser(user)
                .stream()
                .map(pet -> modelMapper.map(pet, FullPetDTO.class)).toList();
        if (fullPetDTOList.isEmpty()) {
            log.info("There are no Pets present in database!");
        } else {
            log.info("{} Pets for user with id : {}, have been fetched from database.", fullPetDTOList.size(), user.getId());
        }
        return fullPetDTOList;
    }

    /**
     * Deletes a pet from a user - Returns the deleted pet.
     * Checks if pet with such id exists.
     *
     * @param petId - the id of the pet we want to delete from a user.
     * @return - FullPetDTO object, containing all the information for the deleted Pet Entity.
     */
    public FullPetDTO deletePetFromUser(UUID petId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(petId);
        System.out.println(petRepository.findById(petId));
        Pet pet = petRepository.findById(petId).orElseThrow(() -> {
            log.warn("Attempted to delete a Pet with id: {} for User with id: {}, which does not exist.", petId,user.getId());
            throw new PetNotFoundException(String.format("Pet with id: %s and owner: %s does not exist!", petId,user.getUsername()));
        });

        petRepository.deleteById(pet.getId());
        log.info("Pet with details : {}, was deleted!", pet);
        return modelMapper.map(pet, FullPetDTO.class);
    }


    /**
     * Updates a pet's property - Returns the updated pet.
     * Checks if pet with such id exists.
     * Checks which property is changed.
     *
     * @param petId - the id of the pet.
     * @param petDTO - the DTO with changed property information.
     * @return - FullPetDTO object, containing all the information about the updated Pet entity.
     */
    public FullPetDTO updatePetProperty(UUID petId, FullPetDTO petDTO) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> {
            log.warn("Attempted to fetch a Pet with id: {} , which does not exist.", petId);
            throw new UserNotFoundException(String.format("Pet with id: %s does not exist!", petId));
        });


        updatePropertyIfNotNull(pet,petDTO.getBreed(),Pet::setBreed);
        updatePropertyIfNotNull(pet,petDTO.getName(),Pet::setName);
        updatePropertyIfNotNull(pet,petDTO.getSpecie(),Pet::setSpecie);

        Pet persistedPet = petRepository.save(pet);

        log.info("Pet with details : {}, was updated!", pet);
        return modelMapper.map(persistedPet, FullPetDTO.class);
    }

    /**
     * Helper method
     *
     * @param pet - pet which parameter is to be changed
     * @param value - current value of a pet's parameter
     * @param setter - setter method of a parameter
     * @param <T> - generic parameter
     */
    private <T> void updatePropertyIfNotNull(Pet pet, T value, BiConsumer<Pet, T> setter) {
        if (value != null) {
            setter.accept(pet, value);
        }
    }
}