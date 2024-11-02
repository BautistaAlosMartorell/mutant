// Archivo de prueba: DnaServiceTest.java
package com.example.mutant.services;

import com.example.mutant.entities.Dna;
import com.example.mutant.repositories.DnaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DnaServiceTest {

    private DnaService dnaService;
    private DnaRepository dnaRepository;

    @BeforeEach
    void setUp() {
        dnaRepository = Mockito.mock(DnaRepository.class);
        dnaService = new DnaService(dnaRepository);
    }

    @Test
    void testIsMutantReturnsTrueForMutantDna() {
        String[] mutantDna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        assertTrue(DnaService.isMutant(mutantDna));
    }

    @Test
    void testIsMutantReturnsFalseForNonMutantDna() {
        String[] nonMutantDna = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};
        assertFalse(DnaService.isMutant(nonMutantDna));
    }

    @Test
    void testIsMutantThrowsExceptionForInvalidDna() {
        String[] invalidDna = {"ATGCGA", "CAGTGC", "TTXTGT", "AGAAGG", "CCCCTA", "TCACTG"};
        assertThrows(IllegalArgumentException.class, () -> DnaService.isMutant(invalidDna));
    }

    @Test
    void testSaveDnaRecordSavesNewDna() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        when(dnaRepository.findByDna(any())).thenReturn(Optional.empty());

        boolean isMutant = dnaService.saveDnaRecord(dna);

        verify(dnaRepository, times(1)).save(any(Dna.class));
        assertTrue(isMutant);
    }

    @Test
    void testSaveDnaRecordReturnsExistingMutantStatus() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        Dna dnaEntity = Dna.builder().dna(String.join(",", dna)).isMutant(true).build();
        when(dnaRepository.findByDna(any())).thenReturn(Optional.of(dnaEntity));

        boolean isMutant = dnaService.saveDnaRecord(dna);

        verify(dnaRepository, never()).save(any(Dna.class));
        assertTrue(isMutant);
    }
}
