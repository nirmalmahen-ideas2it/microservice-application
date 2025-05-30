package com.ideas2it.training.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideas2it.training.user.config.SecurityConfig;
import com.ideas2it.training.user.dto.PagedResponse;
import com.ideas2it.training.user.dto.UserCreateDto;
import com.ideas2it.training.user.dto.UserInfo;
import com.ideas2it.training.user.dto.UserUpdateDto;
import com.ideas2it.training.user.service.UserService;
import com.ideas2it.training.user.service.caching.UserCacheService;
import com.ideas2it.training.user.web.rest.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = {OAuth2ResourceServerAutoConfiguration.class})
@Import({SecurityConfig.class, JwtDecoder.class})
@WithMockUser(roles = "ADMIN")
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.vault.enabled=false",
        "spring.cloud.consul.enabled=false"
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserCacheService userCacheService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testCreate_ValidInput() throws Exception {
        UserCreateDto dto = UserCreateDto.builder().username("test_1").build();
        UserInfo userInfo = UserInfo.builder().build();
        when(userService.create(any(UserCreateDto.class))).thenReturn(userInfo);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate_InvalidInput() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate_ValidInput() throws Exception {
        UserUpdateDto dto = UserUpdateDto.builder().id(1L).build();
        UserInfo userInfo = UserInfo.builder().build();
        when(userService.update(any(UserUpdateDto.class))).thenReturn(userInfo);

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdate_InvalidInput() throws Exception {
        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPatch_ValidInput() throws Exception {
        UserUpdateDto dto = UserUpdateDto.builder().build();
        UserInfo userInfo = UserInfo.builder().build();
        when(userService.partialUpdate(any(UserUpdateDto.class))).thenReturn(userInfo);

        mockMvc.perform(patch("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById_ValidId() throws Exception {
        UserInfo userInfo = UserInfo.builder().build();
        when(userService.getById(1L)).thenReturn(Optional.of(userInfo));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById_InvalidId() throws Exception {
        when(userService.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAll() throws Exception {
        List<UserInfo> users = List.of(UserInfo.builder().build());
        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllPaged() throws Exception {
        PagedResponse<UserInfo> pagedResponse = new PagedResponse<>();
        when(userService.getAllPaged(0, 10)).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/users/paged")
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete_ValidId() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDelete_InvalidId() throws Exception {
        doThrow(new RuntimeException("User not found")).when(userService).delete(999L);

        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isInternalServerError());
    }
}