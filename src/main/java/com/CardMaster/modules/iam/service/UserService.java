package com.CardMaster.modules.iam.service;

import com.CardMaster.modules.iam.repository.UserRepository;
import com.CardMaster.modules.iam.repository.AuditLogRepository;
import com.CardMaster.modules.iam.dto.UserDto;
import com.CardMaster.modules.iam.exception.InvalidCredentialsException;
import com.CardMaster.modules.iam.exception.UserNotFoundException;
import com.CardMaster.modules.iam.entity.AuditLog;
import com.CardMaster.modules.iam.entity.User;
import com.CardMaster.modules.iam.mapper.UserMapper;
import com.CardMaster.modules.iam.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Get all users
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    // Get user by ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    // Register new user
    public User registerUser(User user) {
        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        logAction(savedUser, "REGISTER", "User Registration");
        return savedUser;
    }

    // Login user by email + password
    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logAction(user, "LOGIN_FAILED", "Invalid credentials");
            throw new InvalidCredentialsException();
        }

        logAction(user, "LOGIN", "User Login");
        return jwtUtil.generateToken(user.getUserId(), user.getEmail(), user.getRole().name());
    }

    // Logout user
    public void logoutUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        logAction(user, "LOGOUT", "User Logout");
    }

    // Helper method to save audit logs with full User entity
    private void logAction(User user, String action, String resource) {
        AuditLog log = new AuditLog();
        log.setUser(user); //  always attach full User entity
        log.setAction(action);
        log.setResource(resource);
        log.setMetadata("Performed by " + user.getName());
        auditLogRepository.save(log);
    }
}
