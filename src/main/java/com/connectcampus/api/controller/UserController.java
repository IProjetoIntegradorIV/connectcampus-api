package com.connectcampus.api.controller;

import com.connectcampus.api.model.LoginRequest;
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
    public ResponseEntity<String> createUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Um usuário com esse email já existe.");
        }

        try {
            User savedUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Conta criada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar a conta.");
        }
    }




    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getSenha().equals(loginRequest.getSenha())) {
                return ResponseEntity.ok("Login bem-sucedido!");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos.");
    }


}
