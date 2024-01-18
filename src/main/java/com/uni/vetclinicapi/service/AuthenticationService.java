package com.uni.vetclinicapi.service;

import com.uni.vetclinicapi.persistance.entity.Role;
import com.uni.vetclinicapi.persistance.entity.User;
import com.uni.vetclinicapi.persistance.repository.UserRepository;
import com.uni.vetclinicapi.presentation.exceptions.UsernameAlreadyExistsException;
import com.uni.vetclinicapi.security.util.JwtUtils;
import com.uni.vetclinicapi.service.dto.JwtResponseDTO;
import com.uni.vetclinicapi.service.dto.LoginRequestDTO;
import com.uni.vetclinicapi.service.dto.RegisterRequestDTO;
import com.uni.vetclinicapi.service.dto.RegisterResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This service takes care of the authentication of a user, registration and login operations.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final ModelMapper modelMapper;

    /**
     * This method holds the registration process for a user.
     *
     * @param createUserDto - contains the information for a user needed for registration process.
     */
    public RegisterResponseDTO register(RegisterRequestDTO createUserDto) {
        String username = createUserDto.getUsername();
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    log.warn("Attempted to register a user with username : {}, which already exists.", username);
                    throw new UsernameAlreadyExistsException(String.format("Username %s already exists!", username));
                });
        Role role = roleService.getUserRole(Role.RoleType.CUSTOMER);
        try {
            User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (loggedUser.getAuthorities().contains(Role.RoleType.ADMIN)) {
                role = roleService.getUserRole(Role.RoleType.valueOf(createUserDto.getAuthority()));

            }
        } catch (Exception e) {
            log.warn("No user or admin logged in!");
        }
        User user = new User(
                username,
                passwordEncoder.encode(createUserDto.getPassword()),
                createUserDto.getEmail(),
                createUserDto.getFName(),
                createUserDto.getLName(),
                createUserDto.getPhoneNumber(),
                null,
                Set.of(role));
        userRepository.save(user);
        log.info("User with details : username: {}, email: {}, was successfully registered and saved!", username, createUserDto.getEmail());
        return modelMapper.map(user, RegisterResponseDTO.class);
    }

    /**
     * This method holds the login process for a user.
     *
     * @param loginRequestDTO - contains the information for a user need for login process.
     * @return - JWT Token that's used for authentication.
     */
    public JwtResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        Set<String> role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        log.info("User with username : {}, has been successfully authenticated and jwt token was delivered!", loginRequestDTO.getUsername());
        return new JwtResponseDTO(jwt, role.iterator().next());
    }
}
