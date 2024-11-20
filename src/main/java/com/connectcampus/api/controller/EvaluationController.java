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

    @Autowired
    private EvaluateRepository evaluateRepository;

    @PostMapping("/evaluate")
    public ResponseEntity<ResponseMessage> submitEvaluation(
            @RequestParam String userId,
            @RequestParam String productId,
            @RequestParam float rating) {
        try {
            Evaluate evaluate = new Evaluate(userId, productId, rating);
            evaluateRepository.save(evaluate);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Evaluation submitted successfully."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/evaluations/{productId}")
    public ResponseEntity<List<Evaluate>> getEvaluationsByProductId(@PathVariable String productId) {
        try {
            List<Evaluate> evaluations = evaluateRepository.findByProductId(productId);
            return ResponseEntity.ok(evaluations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
