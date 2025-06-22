package com.ideas2it.training.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideas2it.training.user.domain.User;
import com.ideas2it.training.user.dto.UserInfo;
import com.ideas2it.training.user.dto.UserUpdateDto;
import com.ideas2it.training.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = { "spring.cloud.config.enabled=false", "spring.cache.type=none" })
@AutoConfigureMockMvc
@Transactional
@WithMockUser(roles = "ADMIN")
@Import(NoOpUserCacheServiceConfig.class)
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Insert a test user (or use data.sql if present)
        testUser = new User();
        testUser.setUsername("integrationuser");
        testUser.setEmail("integration@email.com");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);
    }

    @Test
    void testGetUserProfile_Success() throws Exception {
        mockMvc.perform(get("/api/users/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    assertThat(json).contains("integrationuser");
                    assertThat(json).contains("integration@email.com");
                });
    }

    @Test
    void testGetUserProfile_NotFound() throws Exception {
        mockMvc.perform(get("/api/users/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUserProfile_Success() throws Exception {
        UserUpdateDto dto = UserUpdateDto.builder().id(testUser.getId()).email("updated@email.com").build();
        mockMvc.perform(put("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    assertThat(json).contains("updated@email.com");
                });
        // Verify in DB
        User updated = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(updated.getEmail()).isEqualTo("updated@email.com");
    }

    @Test
    void testUpdateUserProfile_ValidationError() throws Exception {
        UserUpdateDto dto = UserUpdateDto.builder().id(null).email("").build();
        mockMvc.perform(put("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUserProfile_NotFound() throws Exception {
        UserUpdateDto dto = UserUpdateDto.builder().id(999999L).email("notfound@email.com").build();
        mockMvc.perform(put("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }
}