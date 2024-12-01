package com.connectcampus.api.controller;

import com.connectcampus.api.model.Evaluate;
import com.connectcampus.api.model.ResponseMessage;
import com.connectcampus.api.repository.EvaluateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation")
public class EvaluationController {

    // Injeta automaticamente o repositório de avaliações, permitindo acesso ao banco de dados
    @Autowired
    private EvaluateRepository evaluateRepository;

    @PostMapping("/evaluate")
    public ResponseEntity<ResponseMessage> submitEvaluation(
            @RequestParam String userId, // ID do usuário que realiza a avaliação
            @RequestParam String productId, // ID do produto avaliado
            @RequestParam float rating) { // Nota dada ao produto
        try {
            // Cria um novo objeto de avaliação com os dados recebidos
            Evaluate evaluate = new Evaluate(userId, productId, rating);
            // Salva a avaliação no banco de dados
            evaluateRepository.save(evaluate);

            // Retorna uma mensagem de sucesso com status HTTP 201 (Created)
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Evaluation submitted successfully."));
        } catch (Exception e) {
            // Captura e exibe qualquer erro no console
            e.printStackTrace();
            // Retorna uma mensagem de erro com status HTTP 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/evaluations/{productId}")
    public ResponseEntity<List<Evaluate>> getEvaluationsByProductId(@PathVariable String productId) {
        try {
            // Busca todas as avaliações associadas ao ID do produto fornecido
            List<Evaluate> evaluations = evaluateRepository.findByProductId(productId);
            // Retorna a lista de avaliações com status HTTP 200 (OK)
            return ResponseEntity.ok(evaluations);
        } catch (Exception e) {
            // Captura e exibe qualquer erro no console
            e.printStackTrace();
            // Retorna uma mensagem de erro com status HTTP 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}

