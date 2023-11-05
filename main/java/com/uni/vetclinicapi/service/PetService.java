package com.uni.vetclinicapi.service;

import com.uni.vetclinicapi.persistance.entity.Pet;
import com.uni.vetclinicapi.persistance.entity.User;
import com.uni.vetclinicapi.persistance.repository.PetRepository;
import com.uni.vetclinicapi.presentation.exceptions.PetAlreadyExistsException;
import com.uni.vetclinicapi.presentation.exceptions.PetNotFoundException;
import com.uni.vetclinicapi.service.dto.FullPetDTO;
import com.uni.vetclinicapi.service.dto.PetDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Provides the necessary methods regarding CRUD operations with Car Entities.
 * Uses the CarRepository as a connection with the database.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PetService {

    private final PetRepository petRepository;

    private final ModelMapper modelMapper;

/*    @PostConstruct
    public void initPets() {
        Pet pet
    }*/

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
            throw new PetAlreadyExistsException(String.format("Pet with the same name: %s and owner: %s already exists!", petDTO.getName(),user.getUsername()));
        }

        Pet pet = modelMapper.map(petDTO, Pet.class);
        Pet persistedPet = petRepository.save(pet);
        log.info("Pet with details : {}, was created!", pet);
        return modelMapper.map(persistedPet, FullPetDTO.class);
    }




    public List<FullPetDTO> findAllPetsForUser() {
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
}