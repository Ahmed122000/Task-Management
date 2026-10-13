package com.sah.taskmanager.controller;

import com.sah.taskmanager.dto.RegisterRequest;
import com.sah.taskmanager.dto.RegisterResponse;
import com.sah.taskmanager.entity.User;
import com.sah.taskmanager.repository.UserRepository;
import com.sah.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerNewUser(@Valid @RequestBody RegisterRequest request){
        User registeredUser = userService.registerNewUser(request);

        RegisterResponse response = new RegisterResponse(
                registeredUser.getId(),
                registeredUser.getUserName(),
                registeredUser.getEmail(),
                registeredUser.getCreatedAt());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
