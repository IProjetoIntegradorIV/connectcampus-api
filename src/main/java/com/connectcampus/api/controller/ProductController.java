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

    // Injeção de dependência do repositório de produtos
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/establishments/{establishmentId}/products")
    public ResponseEntity<List<Product>> getProductsByEstablishmentId(@PathVariable String establishmentId) {
        try {
            // Busca todos os produtos relacionados ao ID do estabelecimento
            List<Product> products = productRepository.findByEstablishmentId(establishmentId);
            return ResponseEntity.ok(products); // Retorna os produtos com status 200 (OK)
        } catch (Exception e) {
            e.printStackTrace(); // Loga o erro no console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Retorna status 500 (Erro interno)
        }
    }

    @PutMapping("/changeProductName")
    public ResponseEntity<ResponseMessage> changeProductName(
            @RequestParam String productId, // ID do produto
            @RequestParam String newName) { // Novo nome do produto
        try {
            // Busca o produto pelo ID
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setName(newName); // Atualiza o nome
                productRepository.save(product); // Salva o produto atualizado no banco de dados
                return ResponseEntity.ok(new ResponseMessage("Product name updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Product not found.")); // Produto não encontrado
            }
        } catch (Exception e) {
            e.printStackTrace(); // Loga o erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage())); // Retorna mensagem de erro
        }
    }

    @PutMapping("/changeProductDescription")
    public ResponseEntity<ResponseMessage> changeProductDescription(
            @RequestParam String productId, // ID do produto
            @RequestParam String newDescription) { // Nova descrição do produto
        try {
            // Busca o produto pelo ID
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setDescription(newDescription); // Atualiza a descrição
                productRepository.save(product); // Salva a alteração no banco
                return ResponseEntity.ok(new ResponseMessage("Product description updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Product not found.")); // Produto não encontrado
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/changeProductPrice")
    public ResponseEntity<ResponseMessage> changeProductPrice(
            @RequestParam String productId, // ID do produto
            @RequestParam String newPrice) { // Novo preço do produto
        try {
            // Busca o produto pelo ID
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setPrice(newPrice); // Atualiza o preço
                productRepository.save(product); // Salva a alteração no banco
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
            @RequestParam String productId, // ID do produto
            @RequestParam String newPhoto) { // Nova URL da foto do produto
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setPhoto(newPhoto); // Atualiza a foto
                productRepository.save(product); // Salva a alteração no banco
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
                productRepository.deleteById(productId); // Deleta o produto do banco de dados
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
            productRepository.save(product); // Salva o novo produto no banco
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
            @RequestParam String productId, // ID do produto
            @RequestParam String newEvaluation) { // Nova avaliação do produto
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setEvaluation(newEvaluation); // Atualiza a avaliação
                productRepository.save(product); // Salva no banco
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
