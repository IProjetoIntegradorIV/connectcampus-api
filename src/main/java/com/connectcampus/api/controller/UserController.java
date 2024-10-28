package com.connectcampus.api.controller;

import com.connectcampus.api.model.LoginRequest;
import com.connectcampus.api.model.LoginResponse;
import com.connectcampus.api.model.ResponseMessage;
import com.connectcampus.api.model.User;
import com.connectcampus.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    public UserController() {
        System.out.println("UserController instantiated");
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseMessage("A user with this email already exists."));
        }

        try {
            User savedUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Account created successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error creating the account."));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                return ResponseEntity.ok(new LoginResponse("Login Successful!", true));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse("Invalid email or password.", false));
    }

}
