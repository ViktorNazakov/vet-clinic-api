package com.uni.vetclinicapi.service;

import com.uni.vetclinicapi.persistance.entity.Pet;
import com.uni.vetclinicapi.persistance.entity.User;
import com.uni.vetclinicapi.persistance.repository.PetRepository;
import com.uni.vetclinicapi.persistance.repository.UserRepository;
import com.uni.vetclinicapi.presentation.exceptions.PetAlreadyExistsException;
import com.uni.vetclinicapi.presentation.exceptions.PetNotFoundException;
import com.uni.vetclinicapi.service.dto.FullPetDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This service is used to load user by username from database.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PetRepository petRepository;

    private final ModelMapper modelMapper;

    @Override
    public User loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found!", username)));
        log.info("User with username : {}, was successfully fetched!", username);
        return user;
    }

    /**
     * Sets the current logged user to an existing pet, which isn't owned by anybody.
     *
     * @param petId - the id of the pet.
     * @return - FullPetDTO with all the information about the given pet.
     */
    public FullPetDTO addPetToUser(UUID petId) {
        Pet petToAddToUser = petRepository.findById(petId).orElseThrow(() -> {
            log.warn("Attempted to fetch pet with id : {}, which doesn't exist.", petId);
            throw new PetNotFoundException(String.format("Pet with id : %s doesn't exist!", petId));
        });
        if (petToAddToUser.getUser() != null) {
            log.warn("Attempted to add to profile pet with id : {}, which is already owned by another user.", petId);
            throw new PetAlreadyExistsException(String.format("Attempted to add to profile Pet with id : %s, which is already owned by another user!", petId));
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        petToAddToUser.setUser(user);
        Pet updatedCar = petRepository.save(petToAddToUser);
        log.info("Successfully added and saved owner (user) with id : {}, to pet with id : {}.", user.getId(), petId);
        return modelMapper.map(updatedCar, FullPetDTO.class);
    }
}
