package com.connectcampus.api.controller;

import com.connectcampus.api.model.*;
import com.connectcampus.api.repository.EstablishmentRepository;
import com.connectcampus.api.repository.ProductRepository;
import com.connectcampus.api.repository.UserRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Value("${google.client.id}")
    private String googleClientId;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                LoginResponse response = new LoginResponse("Login successful.", true);
                return ResponseEntity.ok(response);
            }
        }
        LoginResponse response = new LoginResponse("Credentials invalid.", false);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/login/google")
    public ResponseEntity<String> googleLogin(@RequestBody GoogleLoginRequest googleLoginRequest) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(googleLoginRequest.getIdToken());
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");

                Optional<User> optionalUser = userRepository.findByEmail(email);
                if (optionalUser.isEmpty()) {
                    User newUser = new User(name, email, null, false, pictureUrl);
                    userRepository.save(newUser);
                }

                return ResponseEntity.ok("Google login successful.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google ID Token.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during Google login: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseMessage("User already exists."));
        }

        try {
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Account created successfully."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }


    @Autowired
    private EstablishmentRepository establishmentRepository;

    @GetMapping("/establishments")
    public ResponseEntity<List<Establishment>> getAllEstablishments() {
        try {
            List<Establishment> establishments = establishmentRepository.findAll();
            return ResponseEntity.ok(establishments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/establishments/{id}")
    public ResponseEntity<Establishment> getEstablishmentById(@PathVariable("id") String id) {
        try {
            Optional<Establishment> establishment = establishmentRepository.findById(id);
            return establishment.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/establishments/{establishmentId}/products")
    public ResponseEntity<List<Product>> getProductsByEstablishmentId(@PathVariable String establishmentId) {
        try {
            List<Product> products = productRepository.findByEstablishmentId(establishmentId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/updateProfileImage")
    public ResponseEntity<ResponseMessage> updateProfileImage(
            @RequestParam String email,
            @RequestParam("file") MultipartFile file) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                String base64Image = saveImage(file);
                user.setPhoto(base64Image);

                userRepository.save(user);
                return ResponseEntity.ok(new ResponseMessage("Profile image updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("User not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String base64Image = Base64.encodeBase64String(bytes);
        return base64Image;
    }

    @PutMapping("/changePassword")
    public ResponseEntity<ResponseMessage> changePassword(
            @RequestParam String email,
            @RequestParam String newPassword) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPassword(newPassword);
                userRepository.save(user);

                return ResponseEntity.ok(new ResponseMessage("Password updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("User not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/changeName")
    public ResponseEntity<ResponseMessage> changeName(
            @RequestParam String email,
            @RequestParam String newName) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setName(newName);
                userRepository.save(user);

                return ResponseEntity.ok(new ResponseMessage("Name updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("User not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
    @GetMapping("/profileImage")
    public ResponseEntity<String> getProfileImage(@RequestParam String email) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String base64Image = user.getPhoto();
                if (base64Image != null && !base64Image.isEmpty()) {
                    // Formatar a string Base64 para ser compatível com Glide no Android
                    String imageDataUrl = "data:image/jpeg;base64," + base64Image;
                    return ResponseEntity.ok(imageDataUrl);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("No profile image found for this user.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
*/
    @GetMapping("/checkEstablishmentOwner")
    public ResponseEntity<ResponseMessage> checkEstablishmentOwner(@RequestParam String email) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                boolean isOwner = user.getEstablishmentOwner();
                return ResponseEntity.ok(new ResponseMessage("User establishmentOwner status: " + isOwner));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("User not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

}
