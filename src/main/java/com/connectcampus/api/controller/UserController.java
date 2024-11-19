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

    @GetMapping("/getUserId")
    public ResponseEntity<Map<String, String>> getUserIdByEmail(@RequestParam String email) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                String userId = optionalUser.get().getId();
                Map<String, String> response = new HashMap<>();
                response.put("userId", userId);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error: " + e.getMessage()));
        }
    }


    @GetMapping("/establishments/owner/{userId}")
    public ResponseEntity<Map<String, String>> getEstablishmentIdByOwnerId(@PathVariable String userId) {
        try {
            Optional<Establishment> establishment = establishmentRepository.findByOwnerId(userId);
            if (establishment.isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("establishmentId", establishment.get().getId());
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Establishment not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/isEstablishmentOwner")
    public ResponseEntity<?> isEstablishmentOwner(@RequestParam String email) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                boolean isOwner = optionalUser.get().getEstablishmentOwner();
                Map<String, Boolean> response = new HashMap<>();
                response.put("isEstablishmentOwner", isOwner);
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "User not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/changeEstablishmentName")
    public ResponseEntity<ResponseMessage> changeEstablishmentName(
            @RequestParam String establishmentId,
            @RequestParam String newName) {
        try {
            // Busca o estabelecimento pelo ID
            Optional<Establishment> optionalEstablishment = establishmentRepository.findById(establishmentId);
            if (optionalEstablishment.isPresent()) {
                Establishment establishment = optionalEstablishment.get();
                establishment.setName(newName); // Atualiza o nome do estabelecimento
                establishmentRepository.save(establishment); // Salva a alteração

                return ResponseEntity.ok(new ResponseMessage("Establishment name updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Establishment not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/changeEstablishmentDescription")
    public ResponseEntity<ResponseMessage> changeEstablishmentDescription(
            @RequestParam String establishmentId,
            @RequestParam String newDescription) {
        try {
            // Busca o estabelecimento pelo ID
            Optional<Establishment> optionalEstablishment = establishmentRepository.findById(establishmentId);
            if (optionalEstablishment.isPresent()) {
                Establishment establishment = optionalEstablishment.get();
                establishment.setDescription(newDescription); // Atualiza o nome do estabelecimento
                establishmentRepository.save(establishment); // Salva a alteração

                return ResponseEntity.ok(new ResponseMessage("Establishment description updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Establishment not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/changeEstablishmentOpeningHours")
    public ResponseEntity<ResponseMessage> changeEstablishmentOpeningHours(
            @RequestParam String establishmentId,
            @RequestParam String newOpeningHours) {
        try {
            // Busca o estabelecimento pelo ID
            Optional<Establishment> optionalEstablishment = establishmentRepository.findById(establishmentId);
            if (optionalEstablishment.isPresent()) {
                Establishment establishment = optionalEstablishment.get();
                establishment.setOpeningHours(newOpeningHours); // Atualiza o nome do estabelecimento
                establishmentRepository.save(establishment); // Salva a alteração

                return ResponseEntity.ok(new ResponseMessage("Establishment opening hours updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Establishment not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/changeEstablishmentPhoto")
    public ResponseEntity<ResponseMessage> changeEstablishmenthoto(
            @RequestParam String establishmentId,
            @RequestParam String newPhoto) {
        try {
            // Busca o estabelecimento pelo ID
            Optional<Establishment> optionalEstablishment = establishmentRepository.findById(establishmentId);
            if (optionalEstablishment.isPresent()) {
                Establishment establishment = optionalEstablishment.get();
                establishment.setPhoto(newPhoto); // Atualiza o nome do estabelecimento
                establishmentRepository.save(establishment); // Salva a alteração

                return ResponseEntity.ok(new ResponseMessage("Establishment photo updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Establishment not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/changeProductName")
    public ResponseEntity<ResponseMessage> changeProductName(
            @RequestParam String productId,
            @RequestParam String newName) {
        try {
            // Busca o estabelecimento pelo ID
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setName(newName); // Atualiza o nome do produto
                productRepository.save(product); // Salva a alteração

                return ResponseEntity.ok(new ResponseMessage("Product name updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Product not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/changeProductDescription")
    public ResponseEntity<ResponseMessage> changeProductDescription(
            @RequestParam String productId,
            @RequestParam String newDescription) {
        try {
            // Busca o estabelecimento pelo ID
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setDescription(newDescription); // Atualiza a descrição do produto
                productRepository.save(product); // Salva a alteração

                return ResponseEntity.ok(new ResponseMessage("Product description updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Product not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/changeProductPrice")
    public ResponseEntity<ResponseMessage> changeProductPrice(
            @RequestParam String productId,
            @RequestParam String newPrice) {
        try {
            // Busca o estabelecimento pelo ID
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setPrice(newPrice); // Atualiza o preço do produto
                productRepository.save(product); // Salva a alteração

                return ResponseEntity.ok(new ResponseMessage("Product price updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Product not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/changeProductPhoto")
    public ResponseEntity<ResponseMessage> changeProductPhoto(
            @RequestParam String productId,
            @RequestParam String newPhoto) {
        try {
            // Busca o estabelecimento pelo ID
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setPhoto(newPhoto); // Atualiza a foto do produto
                productRepository.save(product); // Salva a alteração

                return ResponseEntity.ok(new ResponseMessage("Product photo updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Product not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ResponseMessage> deleteProductById(@PathVariable String productId) {
        try {
            Optional<Product> product = productRepository.findById(productId);
            if (product.isPresent()) {
                productRepository.deleteById(productId);
                return ResponseEntity.ok(new ResponseMessage("Product deleted successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Product not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/establishment/{establishmentId}")
    public ResponseEntity<ResponseMessage> deleteEstablishmentById(@PathVariable String establishmentId) {
        try {
            Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
            if (establishment.isPresent()) {
                establishmentRepository.deleteById(establishmentId);
                return ResponseEntity.ok(new ResponseMessage("Establishment deleted successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Establishment not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/establishments/search")
    public ResponseEntity<List<Establishment>> searchEstablishmentsByName(@RequestParam String name) {
        try {
            List<Establishment> establishments = establishmentRepository.findByNameContainingIgnoreCase(name);
            return ResponseEntity.ok(establishments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<ResponseMessage> createProduct(@RequestBody Product product) {
        try {
            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Product created successfully."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }


}
