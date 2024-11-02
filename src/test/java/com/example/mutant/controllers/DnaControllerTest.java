package com.example.mutant.controllers;

import com.example.mutant.dto.DnaRequest;
import com.example.mutant.dto.DnaResponse;
import com.example.mutant.services.DnaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DnaControllerTest {

    private DnaService dnaService;
    private DnaController dnaController;

    @BeforeEach
    void setUp() {
        dnaService = Mockito.mock(DnaService.class);
        dnaController = new DnaController(dnaService);
    }

    @Test
    void checkMutantReturnsOkWhenMutant() {
        DnaRequest dnaRequest = new DnaRequest();
        dnaRequest.setDna(new String[]{"AAAA", "AAAA", "AAAA", "AAAA"});
        when(dnaService.saveDnaRecord(dnaRequest.getDna())).thenReturn(true);

        ResponseEntity<DnaResponse> response = dnaController.checkMutant(dnaRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isMutant());
    }

    @Test
    void checkMutantReturnsForbiddenWhenNotMutant() {
        DnaRequest dnaRequest = new DnaRequest();
        dnaRequest.setDna(new String[]{"ATGC", "ATGC", "ATGC", "ATGC"});
        when(dnaService.saveDnaRecord(dnaRequest.getDna())).thenReturn(false);

        ResponseEntity<DnaResponse> response = dnaController.checkMutant(dnaRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(false, response.getBody().isMutant());
    }
}
