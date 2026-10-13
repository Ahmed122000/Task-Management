package com.sah.taskmanager.service;

import com.sah.taskmanager.dto.RegisterRequest;
import com.sah.taskmanager.entity.User;
import com.sah.taskmanager.exception.EmailAlreadyExistsException;
import com.sah.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(RegisterRequest request){


        String email = request.getEmail();
        if(userRepository.findByEmail(email).isPresent()){
            throw new EmailAlreadyExistsException();
        }

        String username = request.getUserName();
        String passwordHash = passwordEncoder.encode(request.getPassword());

        User newUser = new User();
        newUser.setUserName(username);
        newUser.setEmail(email);
        newUser.setPasswordHash(passwordHash);

        return userRepository.save(newUser);
    }


}

