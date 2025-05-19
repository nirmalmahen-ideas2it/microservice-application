package com.ideas2it.training.user.service.impl;

import com.ideas2it.training.user.domain.PagedResponse;
import com.ideas2it.training.user.domain.Role;
import com.ideas2it.training.user.dto.RoleCreateDto;
import com.ideas2it.training.user.dto.RoleInfo;
import com.ideas2it.training.user.dto.RoleUpdateDto;
import com.ideas2it.training.user.enums.RoleType;
import com.ideas2it.training.user.repository.RoleRepository;
import com.ideas2it.training.user.service.mapper.RoleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_ValidRole() {
        // Arrange
        RoleCreateDto dto = RoleCreateDto.builder().build();
        Role role = new Role();
        RoleInfo roleInfo = RoleInfo.builder().build();

        when(roleMapper.toEntity(dto)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.toInfo(role)).thenReturn(roleInfo);

        // Act
        RoleInfo result = roleService.create(dto);

        // Assert
        assertNotNull(result);
        assertEquals(roleInfo, result);
        verify(roleMapper, times(1)).toEntity(dto);
        verify(roleRepository, times(1)).save(role);
        verify(roleMapper, times(1)).toInfo(role);
    }

    @Test
    void testUpdate_ValidRole() {
        // Arrange
        RoleUpdateDto dto = RoleUpdateDto.builder().id(1L).name(RoleType.USER).build();
        Role role = new Role();
        RoleInfo roleInfo = RoleInfo.builder().build();

        when(roleRepository.findById(dto.getId())).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.toInfo(role)).thenReturn(roleInfo);

        // Act
        RoleInfo result = roleService.update(dto);

        // Assert
        assertNotNull(result);
        assertEquals(roleInfo, result);
        verify(roleRepository, times(1)).findById(dto.getId());
        verify(roleRepository, times(1)).save(role);
        verify(roleMapper, times(1)).toInfo(role);
    }

    @Test
    void testUpdate_RoleNotFound() {
        // Arrange
        RoleUpdateDto dto = RoleUpdateDto.builder().id(1L).build();

        when(roleRepository.findById(dto.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> roleService.update(dto));
        verify(roleRepository, times(1)).findById(dto.getId());
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void testPartialUpdate_ValidRole() {
        // Arrange
        RoleUpdateDto dto = RoleUpdateDto.builder().id(1L).build();
        Role role = new Role();
        RoleInfo roleInfo = RoleInfo.builder().build();

        when(roleRepository.findById(dto.getId())).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.toInfo(role)).thenReturn(roleInfo);

        // Act
        RoleInfo result = roleService.partialUpdate(dto);

        // Assert
        assertNotNull(result);
        assertEquals(roleInfo, result);
        verify(roleRepository, times(1)).findById(dto.getId());
        verify(roleRepository, times(1)).save(role);
        verify(roleMapper, times(1)).toInfo(role);
    }

    @Test
    void testGetById_ValidId() {
        // Arrange
        Long id = 1L;
        Role role = new Role();
        RoleInfo roleInfo = RoleInfo.builder().build();

        when(roleRepository.findById(id)).thenReturn(Optional.of(role));
        when(roleMapper.toInfo(role)).thenReturn(roleInfo);

        // Act
        Optional<RoleInfo> result = roleService.getById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(roleInfo, result.get());
        verify(roleRepository, times(1)).findById(id);
        verify(roleMapper, times(1)).toInfo(role);
    }

    @Test
    void testGetById_InvalidId() {
        // Arrange
        Long id = 1L;

        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<RoleInfo> result = roleService.getById(id);

        // Assert
        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findById(id);
        verifyNoInteractions(roleMapper);
    }

    @Test
    void testGetAll() {
        // Arrange
        List<Role> roles = List.of(new Role());
        List<RoleInfo> roleInfos = List.of(RoleInfo.builder().build());

        when(roleRepository.findAll()).thenReturn(roles);
        when(roleMapper.toInfo(any(Role.class))).thenReturn(roleInfos.get(0));

        // Act
        List<RoleInfo> result = roleService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(roleInfos.size(), result.size());
        verify(roleRepository, times(1)).findAll();
        verify(roleMapper, times(roles.size())).toInfo(any(Role.class));
    }

    @Test
    void testGetAllPaged() {
        // Arrange
        int offset = 0;
        int limit = 10;
        PageRequest pageRequest = PageRequest.of(offset / limit, limit);
        List<Role> roles = List.of(new Role());
        Page<Role> page = new PageImpl<>(roles, pageRequest, roles.size());
        List<RoleInfo> roleInfos = List.of(RoleInfo.builder().build());

        when(roleRepository.findAll(pageRequest)).thenReturn(page);
        when(roleMapper.toInfo(any(Role.class))).thenReturn(roleInfos.get(0));

        // Act
        PagedResponse<RoleInfo> result = roleService.getAllPaged(offset, limit);

        // Assert
        assertNotNull(result);
        assertEquals(roleInfos.size(), result.getItems().size());
        verify(roleRepository, times(1)).findAll(pageRequest);
        verify(roleMapper, times(roles.size())).toInfo(any(Role.class));
    }

    @Test
    void testDelete_ValidId() {
        // Arrange
        Long id = 1L;

        doNothing().when(roleRepository).deleteById(id);

        // Act
        roleService.delete(id);

        // Assert
        verify(roleRepository, times(1)).deleteById(id);
    }
}