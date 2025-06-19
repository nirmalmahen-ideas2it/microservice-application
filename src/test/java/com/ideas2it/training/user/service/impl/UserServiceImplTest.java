package com.ideas2it.training.user.service.impl;

import com.ideas2it.training.user.domain.User;
import com.ideas2it.training.user.dto.PagedResponse;
import com.ideas2it.training.user.dto.UserCreateDto;
import com.ideas2it.training.user.dto.UserInfo;
import com.ideas2it.training.user.dto.UserUpdateDto;
import com.ideas2it.training.user.dto.PasswordResetRequestDto;
import com.ideas2it.training.user.dto.PasswordResetTokenDto;
import com.ideas2it.training.user.dto.PasswordResetResponseDto;
import com.ideas2it.training.user.repository.RoleRepository;
import com.ideas2it.training.user.repository.UserRepository;
import com.ideas2it.training.user.service.caching.UserCacheService;
import com.ideas2it.training.user.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserCacheService userCacheService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_ValidUser() {
        // Arrange
        UserCreateDto dto = UserCreateDto.builder().build();
        User user = new User();
        user.setPassword("encodedPassword");
        User savedUser = new User();
        UserInfo userInfo = UserInfo.builder().build();

        when(userMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toInfo(savedUser)).thenReturn(userInfo);

        // Act
        UserInfo result = userService.create(dto);

        // Assert
        assertNotNull(result);
        assertEquals(userInfo, result);
        verify(userMapper, times(1)).toEntity(dto);
        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(userCacheService, times(1)).saveUser(userInfo);
    }

    @Test
    void testUpdate_ValidUser() {
        // Arrange
        UserUpdateDto dto = UserUpdateDto.builder().build();
        User user = new User();
        User updatedUser = new User();
        UserInfo userInfo = UserInfo.builder().build();

        when(userRepository.findById(dto.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);
        when(userMapper.toInfo(updatedUser)).thenReturn(userInfo);

        // Act
        UserInfo result = userService.update(dto);

        // Assert
        assertNotNull(result);
        assertEquals(userInfo, result);
        verify(userRepository, times(1)).findById(dto.getId());
        verify(userRepository, times(1)).save(user);
        verify(userCacheService, times(1)).saveUser(userInfo);
    }

    @Test
    void testUpdate_UserNotFound() {
        // Arrange
        UserUpdateDto dto = UserUpdateDto.builder().build();
        when(userRepository.findById(dto.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.update(dto));
        verify(userRepository, times(1)).findById(dto.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testGetById_UserInCache() {
        // Arrange
        Long id = 1L;
        UserInfo cachedUser = UserInfo.builder().build();
        when(userCacheService.getUser(id)).thenReturn(cachedUser);

        // Act
        Optional<UserInfo> result = userService.getById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(cachedUser, result.get());
        verify(userCacheService, times(1)).getUser(id);
        verifyNoInteractions(userRepository);
    }

    @Test
    void testGetById_UserNotInCache() {
        // Arrange
        Long id = 1L;
        User user = new User();
        UserInfo userInfo = UserInfo.builder().build();
        when(userCacheService.getUser(id)).thenReturn(null);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toInfo(user)).thenReturn(userInfo);

        // Act
        Optional<UserInfo> result = userService.getById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userInfo, result.get());
        verify(userCacheService, times(1)).getUser(id);
        verify(userRepository, times(1)).findById(id);
        verify(userCacheService, times(1)).saveUser(userInfo);
    }

    @Test
    void testGetAll() {
        // Arrange
        List<User> users = List.of(new User());
        List<UserInfo> userInfos = List.of(UserInfo.builder().build());
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toInfo(any(User.class))).thenReturn(userInfos.get(0));

        // Act
        List<UserInfo> result = userService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(userInfos.size(), result.size());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(users.size())).toInfo(any(User.class));
    }

    @Test
    void testGetAllPaged() {
        // Arrange
        int offset = 0;
        int limit = 10;
        PageRequest pageRequest = PageRequest.of(offset / limit, limit);
        List<User> users = List.of(new User());
        Page<User> page = new PageImpl<>(users, pageRequest, users.size());
        List<UserInfo> userInfos = List.of(UserInfo.builder().build());

        when(userRepository.findAll(pageRequest)).thenReturn(page);
        when(userMapper.toInfo(any(User.class))).thenReturn(userInfos.get(0));

        // Act
        PagedResponse<UserInfo> result = userService.getAllPaged(offset, limit);

        // Assert
        assertNotNull(result);
        assertEquals(userInfos.size(), result.getItems().size());
        verify(userRepository, times(1)).findAll(pageRequest);
        verify(userMapper, times(users.size())).toInfo(any(User.class));
    }

    @Test
    void testDelete_ValidId() {
        // Arrange
        Long id = 1L;

        // Act
        userService.delete(id);

        // Assert
        verify(userRepository, times(1)).deleteById(id);
        verify(userCacheService, times(1)).deleteUser(id);
    }

    @Test
    void testRequestPasswordReset_ValidUsername() {
        PasswordResetRequestDto requestDto = new PasswordResetRequestDto();
        requestDto.setEmailOrUsername("testuser");
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        PasswordResetResponseDto response = userService.requestPasswordReset(requestDto);
        assertTrue(response.getMessage().contains("Password reset token generated"));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRequestPasswordReset_ValidEmail() {
        PasswordResetRequestDto requestDto = new PasswordResetRequestDto();
        requestDto.setEmailOrUsername("test@email.com");
        User user = new User();
        user.setEmail("test@email.com");
        when(userRepository.findByUsername("test@email.com")).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        PasswordResetResponseDto response = userService.requestPasswordReset(requestDto);
        assertTrue(response.getMessage().contains("Password reset token generated"));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRequestPasswordReset_UserNotFound() {
        PasswordResetRequestDto requestDto = new PasswordResetRequestDto();
        requestDto.setEmailOrUsername("unknown");
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(List.of());
        PasswordResetResponseDto response = userService.requestPasswordReset(requestDto);
        assertTrue(response.getMessage().contains("If the user exists"));
    }

    @Test
    void testRequestPasswordReset_InvalidInput() {
        PasswordResetRequestDto requestDto = new PasswordResetRequestDto();
        requestDto.setEmailOrUsername("");
        assertThrows(IllegalArgumentException.class, () -> userService.requestPasswordReset(requestDto));
    }

    @Test
    void testResetPassword_ValidToken() {
        PasswordResetTokenDto tokenDto = new PasswordResetTokenDto();
        tokenDto.setToken("valid-token");
        tokenDto.setNewPassword("newPassword123");
        User user = new User();
        user.setResetToken("valid-token");
        user.setResetTokenExpiry(Instant.now().plusSeconds(60));
        user.setId(1L);
        when(userRepository.findByResetToken("valid-token")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword123")).thenReturn("encoded");
        when(userRepository.save(user)).thenReturn(user);
        PasswordResetResponseDto response = userService.resetPassword(tokenDto);
        assertTrue(response.getMessage().contains("Password reset successful"));
        verify(userRepository, times(1)).save(user);
        verify(userCacheService, times(1)).deleteUser(1L);
    }

    @Test
    void testResetPassword_InvalidToken() {
        PasswordResetTokenDto tokenDto = new PasswordResetTokenDto();
        tokenDto.setToken("invalid-token");
        tokenDto.setNewPassword("newPassword123");
        when(userRepository.findByResetToken("invalid-token")).thenReturn(Optional.empty());
        PasswordResetResponseDto response = userService.resetPassword(tokenDto);
        assertTrue(response.getMessage().contains("Invalid or expired token"));
    }

    @Test
    void testResetPassword_ExpiredToken() {
        PasswordResetTokenDto tokenDto = new PasswordResetTokenDto();
        tokenDto.setToken("expired-token");
        tokenDto.setNewPassword("newPassword123");
        User user = new User();
        user.setResetToken("expired-token");
        user.setResetTokenExpiry(Instant.now().minusSeconds(60));
        when(userRepository.findByResetToken("expired-token")).thenReturn(Optional.of(user));
        PasswordResetResponseDto response = userService.resetPassword(tokenDto);
        assertTrue(response.getMessage().contains("Token expired"));
    }

    @Test
    void testResetPassword_InvalidInput() {
        PasswordResetTokenDto tokenDto = new PasswordResetTokenDto();
        tokenDto.setToken("");
        tokenDto.setNewPassword("");
        assertThrows(IllegalArgumentException.class, () -> userService.resetPassword(tokenDto));
    }
}