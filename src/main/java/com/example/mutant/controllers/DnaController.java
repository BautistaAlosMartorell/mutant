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
@RequestMapping("/mutant")
public class DnaController {

    private final DnaService dnaService;

    @Autowired
    public DnaController(DnaService dnaService) {
        this.dnaService = dnaService;
    }

    @PostMapping
    public ResponseEntity<DnaResponse> checkMutant(@Valid @RequestBody DnaRequest dnaRequest) {
        boolean isMutant = dnaService.saveDnaRecord(dnaRequest.getDna());
        DnaResponse dnaResponse = new DnaResponse(isMutant);

        return isMutant
                ? ResponseEntity.ok(dnaResponse)
                : ResponseEntity.status(HttpStatus.FORBIDDEN).body(dnaResponse);
    }
}
