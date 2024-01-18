package com.uni.vetclinicapi.service;

import com.uni.vetclinicapi.persistance.entity.Role;
import com.uni.vetclinicapi.persistance.entity.User;
import com.uni.vetclinicapi.persistance.repository.PetRepository;
import com.uni.vetclinicapi.persistance.repository.RoleRepository;
import com.uni.vetclinicapi.persistance.repository.UserRepository;
import com.uni.vetclinicapi.persistance.repository.VisitRepository;
import com.uni.vetclinicapi.presentation.exceptions.InvalidAuthoritiesException;
import com.uni.vetclinicapi.presentation.exceptions.UserNotFoundException;
import com.uni.vetclinicapi.service.dto.UserInfoDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * This service is used to load user by username from database.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final VisitRepository visitRepository;

    private final PetRepository petRepository;

    private final ModelMapper modelMapper;

    @Override
    public User loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found!", username)));
        log.info("User with username : {}, was successfully fetched!", username);
        return user;
    }

/*    *//**
     * Sets the current logged user to an existing pet, which isn't owned by anybody.
     *
     * @param petId - the id of the pet.
     * @return - FullPetDTO with all the information about the given pet.
     *//*
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
    }*/

    /**
     * Returns currently logged-in user's info.
     *
     * @return - UserInfoDTO with all the information about the user.
     */
    public UserInfoDTO getLoggedUserInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> role = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        UserInfoDTO userInfoDTO = modelMapper.map(user, UserInfoDTO.class);
        userInfoDTO.setRole(role.iterator().next());
        return userInfoDTO;
    }

    /**
     * Returns a user.
     *
     * @return - A UserInfoDTO with all the information the user.
     */
    public UserInfoDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Attempted to fetch a User with id: {} , which does not exist.", userId);
            throw new UserNotFoundException(String.format("User with id: %s does not exist!", userId));
        });

        log.info("User with details : {}, was fetched!", user);
        return modelMapper.map(user, UserInfoDTO.class);
    }

    /**
     * Returns a list with all users.
     *
     * @return - List of UserInfoDTO with all the information about a user.
     */
    public List<UserInfoDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserInfoDTO> userInfoDTOList = new ArrayList<>();
        users.forEach(user -> {
            Set<String> role = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            UserInfoDTO userInfo = modelMapper.map(user,UserInfoDTO.class);
            userInfo.setRole(role.iterator().next());
            userInfoDTOList.add(userInfo);

        });
        return userInfoDTOList;
    }

    /**
     * Deletes a user - Returns the deleted user.
     * Checks if user with such id exists.
     *
     * @param userId - the id of the user to delete.
     * @return - UserInfoDTO object, containing all the information about the deleted User entity.
     */
    @Transactional
    public UserInfoDTO deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Attempted to delete a User with id: {} , which does not exist.", userId);
            throw new UserNotFoundException(String.format("User with id: %s does not exist!", userId));
        });

        visitRepository.deleteAllByUser(user);
        petRepository.deleteAllByUser(user);
        userRepository.deleteById(user.getId());
        log.info("User with details : {}, was deleted!", user);
        return modelMapper.map(user, UserInfoDTO.class);
    }

    /**
     * Updates a user's property - Returns the updated user.
     * Checks if user with such id exists.
     * Checks which property is changed.
     *
     * @param userId - the id of the user to update.
     * @return - UserInfoDTO object, containing all the information about the updated User entity.
     */
    public UserInfoDTO updateUserProperty(UUID userId, UserInfoDTO userInfoDTO) {
        Optional<Role> adminRole = roleRepository.findByAuthority(Role.RoleType.ADMIN);
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Attempted to update a User with id: {} , which does not exist.", userId);
            throw new UserNotFoundException(String.format("User with id: %s does not exist!", userId));
        });
        if (!loggedUser.getId().equals(userId) && !loggedUser.getAuthorities().contains(adminRole.get())) {
            log.warn("Attempted to update a User with id: {} , but currently logged User does not have the authority.", userId);
            throw new InvalidAuthoritiesException(String.format("User with id: %s cannot be updated, because currently logged User does not have the authority!", userId));
        }
        updatePropertyIfNotNull(user,userInfoDTO.getFName(),User::setFName);
        updatePropertyIfNotNull(user,userInfoDTO.getLName(),User::setLName);
        updatePropertyIfNotNull(user,userInfoDTO.getPhoneNumber(),User::setPhoneNumber);

        User persistedUser = userRepository.save(user);

        log.info("User with details : {}, was updated!", user);
        Set<String> role = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        UserInfoDTO userInfoDTOResponse = modelMapper.map(persistedUser, UserInfoDTO.class);
        userInfoDTO.setRole(role.iterator().next());
        return userInfoDTOResponse;
    }

    private <T> void updatePropertyIfNotNull(User user, T value, BiConsumer<User, T> setter) {
        if (value != null) {
            setter.accept(user, value);
        }
    }

    /**
     * Returns a list with all users with role VET.
     *
     * @return - List of UserInfoDTO with all the information about a user with role VET.
     */
    public List<UserInfoDTO> getAllUsersWithRoleVet() {
        Optional<Role> vetRole = roleRepository.findByAuthority(Role.RoleType.VET);
        List<User> users = userRepository.findAll();
        List<User> vets = users.stream().filter(user -> user.getAuthorities().contains(vetRole.get())).toList();
        List<UserInfoDTO> userInfoDTOList = new ArrayList<>();
        vets.forEach(user -> {
            Set<String> role = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            UserInfoDTO userInfo = modelMapper.map(user,UserInfoDTO.class);
            userInfo.setRole(role.iterator().next());
            userInfoDTOList.add(userInfo);

        });
        return userInfoDTOList;
    }
}
