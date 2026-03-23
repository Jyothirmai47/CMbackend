package com.CardMaster.modules.iam.service;

import com.CardMaster.modules.iam.enums.UserEnum;
import com.CardMaster.modules.iam.repository.UserRepository;
import com.CardMaster.modules.iam.repository.AuditLogRepository;
import com.CardMaster.modules.iam.exception.InvalidCredentialsException;
import com.CardMaster.modules.iam.exception.UserNotFoundException;
import com.CardMaster.modules.iam.entity.User;
import com.CardMaster.modules.iam.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setName("Priya");
        testUser.setEmail("priya@example.com");
        testUser.setPassword("secret123");
        testUser.setRole(UserEnum.CUSTOMER); // use  enum
    }

    @Test
    void testRegisterUser_success() {
        when(passwordEncoder.encode("secret123")).thenReturn("encodedSecret123");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User saved = userService.registerUser(testUser);

        assertEquals("Priya", saved.getName());
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testLoginUser_success() {
        when(userRepository.findByEmail("priya@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("secret123", "secret123")).thenReturn(true);
        when(jwtUtil.generateToken(1L, "priya@example.com", "CUSTOMER")).thenReturn("mockToken");

        String token = userService.loginUser("priya@example.com", "secret123");

        assertEquals("mockToken", token);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testLoginUser_invalidPassword() {
        when(userRepository.findByEmail("priya@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPass", "secret123")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> userService.loginUser("priya@example.com", "wrongPass"));

        verify(auditLogRepository, times(1)).save(any()); // LOGIN_FAILED logged
    }

    @Test
    void testLoginUser_userNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> userService.loginUser("missing@example.com", "secret123"));

        verify(auditLogRepository, never()).save(any());
    }

    @Test
    void testLogoutUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        userService.logoutUser(1L);

        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testLogoutUser_userNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.logoutUser(99L));

        verify(auditLogRepository, never()).save(any());
    }
}
