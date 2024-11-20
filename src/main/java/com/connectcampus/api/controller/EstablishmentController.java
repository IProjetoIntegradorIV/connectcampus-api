package com.connectcampus.api.controller;

import com.connectcampus.api.model.Establishment;
import com.connectcampus.api.model.ResponseMessage;
import com.connectcampus.api.repository.EstablishmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/establishment")
public class EstablishmentController {
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

}
