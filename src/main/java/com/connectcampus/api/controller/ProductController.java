package com.connectcampus.api.controller;

import com.connectcampus.api.model.Product;
import com.connectcampus.api.model.ResponseMessage;
import com.connectcampus.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {
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

    @PutMapping("/changeProductEvaluation")
    public ResponseEntity<ResponseMessage> changeProductEvaluation(
            @RequestParam String productId,
            @RequestParam String newEvaluation) {
        try {
            // Busca o produto pelo ID
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setEvaluation(newEvaluation); // Atualiza a avaliação do produto
                productRepository.save(product); // Salva a alteração

                return ResponseEntity.ok(new ResponseMessage("Product evaluation updated successfully."));
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
}
