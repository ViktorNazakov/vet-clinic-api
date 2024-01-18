package com.uni.vetclinicapi.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.vetclinicapi.persistance.repository.UserRepository;
import com.uni.vetclinicapi.security.util.JwtUtils;
import com.uni.vetclinicapi.service.dto.JwtResponseDTO;
import com.uni.vetclinicapi.service.dto.LoginRequestDTO;
import com.uni.vetclinicapi.service.dto.RegisterRequestDTO;
import com.uni.vetclinicapi.service.dto.RegisterResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AuthenticationControllerTest {

    private static final String REGISTER_ENDPOINT = "/api/v1/auth/register";
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void executeAuthenticationControllerRegister_ShouldCreateNewUser_CREATED() throws Exception {
        // Arrange
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("viktor", "viktor@gmail.com", "viktor");
        // Act
        MvcResult response = mvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        RegisterResponseDTO registerResponseDTO = objectMapper.readValue(response.getResponse().getContentAsString(), RegisterResponseDTO.class);
        //Assert
        Assertions.assertEquals(registerRequestDTO.getUsername(), registerResponseDTO.getUsername());
        Assertions.assertTrue(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent());
    }

    @Test
    void executeAuthenticationControllerRegister_ShouldNotCreateInvalidUser_BAD_REQUEST() throws Exception {
        // Arrange
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("", "", "");
        // Act
        mvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isBadRequest());
        // Assert
        Assertions.assertTrue(userRepository.findByUsername(registerRequestDTO.getUsername()).isEmpty());
    }

    @Test
    void executeAuthenticationControllerRegister_ShouldNotCreateNewUserWithExistingUsername_CONFLICT() throws Exception {
        // Arrange
        long expectedValue = 1;
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("viktor", "viktor@gmail.com", "viktor");
        mvc.perform(post(REGISTER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequestDTO)));
        // Act
        mvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isConflict());
        long actualValue = userRepository.findAll().stream().filter(user -> user.getUsername().equals(registerRequestDTO.getUsername())).count();
        //Assert
        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    void executeAuthenticationControllerLogin_ShouldSuccessfullyAuthenticateUserAndReturnJWT_OK() throws Exception {
        // Arrange
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("viktor", "viktor@gmail.com", "viktor");
        mvc.perform(post(REGISTER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequestDTO)));
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("viktor", "viktor");
        String expectedUsername = loginRequestDTO.getUsername();
        // Act
        MvcResult response = mvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();
        JwtResponseDTO jwtResponseDTO = objectMapper.readValue(response.getResponse().getContentAsString(), JwtResponseDTO.class);
        String actualUsername = jwtUtils.getUserNameFromJwtToken(jwtResponseDTO.getToken());
        //Assert
        Assertions.assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void executeAuthenticationControllerLogin_ShouldNotAuthenticateNonExistingUser_UNAUTHORIZED() throws Exception {
        // Arrange
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("viktor", "viktor");
        // Act + Assert
        mvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isUnauthorized());
    }
}