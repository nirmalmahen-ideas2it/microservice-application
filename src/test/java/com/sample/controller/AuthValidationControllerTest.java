package com.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.config.SecurityConfig;
import com.sample.dto.AuthRequest;
import com.sample.dto.AuthValidationResponse;
import com.sample.enums.AuthStatus;
import com.sample.service.auth.AuthValidationService;
import com.sample.web.rest.controller.AuthValidationController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthValidationController.class, excludeAutoConfiguration = {OAuth2ResourceServerAutoConfiguration.class})
@Import({SecurityConfig.class})
@WithMockUser(roles = "USER")
class AuthValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthValidationService authService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void testValidate_ValidRequest() throws Exception {
        // Arrange
        AuthRequest request = new AuthRequest("username", "password");
        AuthValidationResponse response = new AuthValidationResponse(AuthStatus.SUCCESS, "Validation successful");
        Mockito.when(authService.validateUser(any(AuthRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(AuthStatus.SUCCESS.name()))
                .andExpect(jsonPath("$.message").value("Validation successful"));
    }

    @Test
    void testValidate_InvalidRequest() throws Exception {
        // Arrange
        AuthRequest request = new AuthRequest("", ""); // Invalid request
        Mockito.when(authService.validateUser(any(AuthRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid authentication request"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testValidate_InternalServerError() throws Exception {
        // Arrange
        AuthRequest request = new AuthRequest("username", "password");
        Mockito.when(authService.validateUser(any(AuthRequest.class)))
                .thenThrow(new RuntimeException("Internal server error"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }
}