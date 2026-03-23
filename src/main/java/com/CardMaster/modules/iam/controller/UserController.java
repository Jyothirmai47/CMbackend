package com.CardMaster.modules.iam.controller;

import java.util.List;

import com.CardMaster.common.dto.ApiResponse;
import com.CardMaster.modules.iam.dto.UserDto;
import com.CardMaster.modules.iam.dto.LoginRequestDto;
import com.CardMaster.modules.iam.mapper.UserMapper;
import com.CardMaster.modules.iam.entity.User;
import com.CardMaster.modules.iam.security.JwtUtil;
import com.CardMaster.modules.iam.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // GET all users
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        log.info("Inside getAllUsers Controller");
        List<UserDto> users = userService.getAllUsers();

        ApiResponse<List<UserDto>> res = new ApiResponse<>();
        res.setMsg("Users Retrieved Successfully");
        res.setData(users);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // GET user by userId
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long userId) {
        log.info("Inside getUserById Controller");
        User user = userService.getUserById(userId);

        ApiResponse<UserDto> res = new ApiResponse<>();
        res.setMsg("User Retrieved Successfully");
        res.setData(UserMapper.toDto(user));
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // POST register new user
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody User user) {
        log.info("Inside register user Controller");
        User saved = userService.registerUser(user);

        ApiResponse<UserDto> resp = new ApiResponse<>();
        resp.setMsg("User created successfully");
        resp.setData(UserMapper.toDto(saved));
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // POST login (using email + password)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        log.info("Inside login Controller");

        String token = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

        ApiResponse<String> r = new ApiResponse<>();
        r.setMsg("Login Successful");
        r.setData("Bearer " + token);
        return ResponseEntity.status(HttpStatus.OK).body(r);
    }

    // POST logout
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7)); //  returns Long
        userService.logoutUser(userId); //  pass directly

        ApiResponse<String> r = new ApiResponse<>();
        r.setMsg("Logout Successful");
        r.setData("Goodbye " + userId);
        return ResponseEntity.status(HttpStatus.OK).body(r);
    }

}
