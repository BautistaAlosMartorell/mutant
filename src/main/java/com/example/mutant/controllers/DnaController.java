package com.example.mutant.controllers;

import jakarta.validation.Valid;
import com.example.mutant.dto.DnaRequest;
import com.example.mutant.dto.DnaResponse;
import com.example.mutant.services.DnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/mutant")
public class DnaController {

    private final DnaService dnaService;

    @Autowired
    public DnaController(DnaService dnaService) {
        this.dnaService = dnaService;
    }

    @PostMapping("/verify")
    public ResponseEntity<DnaResponse> verifyMutantStatus(@Valid @RequestBody DnaRequest dnaRequest) {
        boolean mutantDetected = dnaService.saveDnaRecord(dnaRequest.getDna());
        DnaResponse dnaResponse = new DnaResponse(mutantDetected);

        HttpStatus status = mutantDetected ? HttpStatus.OK : HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(dnaResponse, status);
    }
}