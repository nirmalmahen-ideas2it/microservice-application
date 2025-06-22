package com.ideas2it.training.user.controller;

import com.ideas2it.training.user.config.SecurityConfig;
import com.ideas2it.training.user.dto.RoleCreateDto;
import com.ideas2it.training.user.dto.RoleInfo;
import com.ideas2it.training.user.service.RoleService;
import com.ideas2it.training.user.web.rest.controller.RoleController;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(properties = { "spring.cloud.config.enabled=false", "spring.cache.type=none" },controllers = RoleController.class, excludeAutoConfiguration = {OAuth2ResourceServerAutoConfiguration.class})
@Import({SecurityConfig.class, JwtDecoder.class})
@WithMockUser(roles = "ADMIN")
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private RoleService roleService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testCreateRole_ValidInput() throws Exception {
        // Arrange
        RoleInfo roleInfo = RoleInfo.builder().build();
        when(roleService.create(any(RoleCreateDto.class))).thenReturn(roleInfo);

        // Act & Assert
        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ADMIN\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateRole_InvalidInput() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetRoleById_ValidId() throws Exception {
        // Arrange
        RoleInfo roleInfo = RoleInfo.builder().build();
        when(roleService.getById(1L)).thenReturn(java.util.Optional.of(roleInfo));

        // Act & Assert
        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetRoleById_InvalidId() throws Exception {
        // Arrange
        when(roleService.getById(999L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/roles/999"))
                .andExpect(status().isNotFound());
    }
}