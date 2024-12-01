package com.connectcampus.api.controller;

import com.connectcampus.api.model.*;
import com.connectcampus.api.repository.UserRepository;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Value("${google.client.id}")
    private String googleClientId;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        // Busca o usuário no repositório pelo email fornecido.
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Verifica se a senha fornecida corresponde à armazenada.
            if (user.getPassword().equals(loginRequest.getPassword())) {
                LoginResponse response = new LoginResponse("Login successful.", true);
                return ResponseEntity.ok(response); // Retorna sucesso se as credenciais forem válidas.
            }
        }
        // Retorna erro de autenticação caso o email ou senha sejam inválidos.
        LoginResponse response = new LoginResponse("Credentials invalid.", false);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/login/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequest googleLoginRequest) {
        try {
            // Configura o verificador de tokens do Google.
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(googleClientId)) // ID do cliente configurado.
                    .build();

            // Verifica o token fornecido.
            GoogleIdToken idToken = verifier.verify(googleLoginRequest.getIdToken());
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();

                // Cria um novo usuário se ele ainda não existir.
                Optional<User> optionalUser = userRepository.findByEmail(email);
                if (optionalUser.isEmpty()) {
                    User newUser = new User((String) payload.get("name"), email, null, false, (String) payload.get("picture"));
                    userRepository.save(newUser);
                }

                return ResponseEntity.ok(email); // Retorna o email autenticado.
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google ID Token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during Google login: " + e.getMessage());
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<ResponseMessage> createUser(@RequestBody User user) {
        // Verifica se já existe um usuário com o mesmo email.
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            // Se o email já existir, retorna um erro de conflito (HTTP 409).
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseMessage("User already exists."));
        }

        try {
            // Se o email não existir, salva o novo usuário no banco de dados.
            userRepository.save(user);
            // Retorna uma resposta de sucesso com o status HTTP 201 (Criado).
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Account created successfully."));
        } catch (Exception e) {
            // Em caso de erro, retorna o status HTTP 500 (Erro Interno) com a mensagem do erro.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<ResponseMessage> changePassword(
            @RequestParam String email,
            @RequestParam String newPassword) {
        try {
            // Busca o usuário pelo email fornecido.
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPassword(newPassword); // Atualiza a senha do usuário.
                userRepository.save(user); // Salva as alterações.

                // Retorna uma resposta de sucesso com status HTTP 200 (OK).
                return ResponseEntity.ok(new ResponseMessage("Password updated successfully."));
            } else {
                // Se o usuário não for encontrado, retorna um erro com status HTTP 404.
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("User not found."));
            }
        } catch (Exception e) {
            // Em caso de erro, retorna o status HTTP 500 com a mensagem do erro.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/changeName")
    public ResponseEntity<ResponseMessage> changeName(
            @RequestParam String email,
            @RequestParam String newName) {
        try {
            // Busca o usuário pelo email.
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setName(newName); // Atualiza o nome do usuário.
                userRepository.save(user); // Salva as alterações.

                // Retorna uma resposta de sucesso com status HTTP 200 (OK).
                return ResponseEntity.ok(new ResponseMessage("Name updated successfully."));
            } else {
                // Se o usuário não for encontrado, retorna um erro com status HTTP 404.
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("User not found."));
            }
        } catch (Exception e) {
            // Em caso de erro, retorna o status HTTP 500 com a mensagem do erro.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        // Busca o usuário pelo email.
        Optional<User> user = userRepository.findByEmail(email);
        // Se o usuário for encontrado, retorna com status HTTP 200 e o usuário.
        return user.map(ResponseEntity::ok)
                // Caso contrário, retorna um status HTTP 404 (Não encontrado).
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/checkEstablishmentOwner")
    public ResponseEntity<ResponseMessage> checkEstablishmentOwner(@RequestParam String email) {
        try {
            // Busca o usuário pelo email.
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                boolean isOwner = user.getEstablishmentOwner(); // Verifica se o usuário é dono de um estabelecimento.

                // Retorna uma resposta com status HTTP 200 e o status de proprietário.
                return ResponseEntity.ok(new ResponseMessage("User establishmentOwner status: " + isOwner));
            } else {
                // Se o usuário não for encontrado, retorna um erro com status HTTP 404.
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("User not found."));
            }
        } catch (Exception e) {
            // Em caso de erro, retorna o status HTTP 500 com a mensagem do erro.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/getUserId")
    public ResponseEntity<Map<String, String>> getUserIdByEmail(@RequestParam String email) {
        try {
            // Busca o usuário pelo email.
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                String userId = optionalUser.get().getId(); // Obtém o ID do usuário.
                Map<String, String> response = new HashMap<>();
                response.put("userId", userId); // Adiciona o ID ao mapa de resposta.

                // Retorna o ID com status HTTP 200 (OK).
                return ResponseEntity.ok(response);
            } else {
                // Se o usuário não for encontrado, retorna um erro com status HTTP 404.
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found."));
            }
        } catch (Exception e) {
            // Em caso de erro, retorna o status HTTP 500 com a mensagem do erro.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error: " + e.getMessage()));
        }
    }

    @PutMapping("/changeUserPhoto")
    public ResponseEntity<ResponseMessage> changeUserPhoto(
            @RequestParam String userId,
            @RequestParam String newPhoto) {
        try {
            // Busca o usuário pelo ID.
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPhoto(newPhoto); // Atualiza a foto do usuário.
                userRepository.save(user); // Salva as alterações.

                // Retorna uma resposta de sucesso com status HTTP 200 (OK).
                return ResponseEntity.ok(new ResponseMessage("User photo updated successfully."));
            } else {
                // Se o usuário não for encontrado, retorna um erro com status HTTP 404.
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("User not found."));
            }
        } catch (Exception e) {
            // Em caso de erro, retorna o status HTTP 500 com a mensagem do erro.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/isEstablishmentOwner")
    public ResponseEntity<?> isEstablishmentOwner(@RequestParam String email) {
        // Tenta buscar o usuário pelo e-mail fornecido como parâmetro
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);

            // Verifica se o usuário foi encontrado no banco de dados
            if (optionalUser.isPresent()) {
                // Se o usuário existir, verifica se ele é proprietário de um estabelecimento
                boolean isOwner = optionalUser.get().getEstablishmentOwner();

                // Cria um mapa para enviar a resposta
                Map<String, Boolean> response = new HashMap<>();
                response.put("isEstablishmentOwner", isOwner);  // Adiciona o status de proprietário no mapa

                // Retorna a resposta com status HTTP 200 OK e o mapa contendo o status de proprietário
                return ResponseEntity.ok(response);
            } else {
                // Se o usuário não for encontrado, cria uma resposta de erro
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "User not found.");

                // Retorna o status HTTP 404 (não encontrado) com a mensagem de erro
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            // Cria uma resposta de erro com uma mensagem contendo detalhes do erro ocorrido
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error: " + e.getMessage());

            // Retorna o status HTTP 500 (erro interno do servidor) com a mensagem de erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
