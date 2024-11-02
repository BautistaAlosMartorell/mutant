// Ruta del archivo: com/example/mutant/services/DnaService.java
package com.example.mutant.services;

import com.example.mutant.entities.Dna;
import com.example.mutant.repositories.DnaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class DnaService {

    private final DnaRepository dnaRepo;
    private static final int MIN_SEQUENCE_LENGTH = 4;

    public DnaService(DnaRepository dnaRepo) {
        this.dnaRepo = dnaRepo;
    }

    // reviso si el ADN es mutante
    public static boolean isMutant(String[] dna) {
        validateDnaFormat(dna); // valido que el ADN sea cuadrado y válido

        // cuento secuencias en filas, columnas y diagonales
        int sequenceMatches = IntStream.of(
                findHorizontalSequences(dna),
                findVerticalSequences(dna),
                findDiagonalSequences(dna)
        ).parallel().sum();

        return sequenceMatches > 1; // si hay más de una secuencia, es mutante
    }

    // chequeo formato cuadrado y letras válidas
    private static void validateDnaFormat(String[] dna) {
        if (dna == null || dna.length == 0 || !IntStream.range(0, dna.length)
                .allMatch(i -> dna[i] != null && dna[i].matches("[ATGC]+") && dna[i].length() == dna.length)) {
            throw new IllegalArgumentException("formato de adn no válido, solo A, T, G y C en estructura NxN");
        }
    }

    // busco secuencias en filas
    private static int findHorizontalSequences(String[] dna) {
        int matchCount = 0;
        for (String row : dna) {
            matchCount += sequenceCounter(row);
        }
        return matchCount;
    }

    // busco secuencias en columnas
    private static int findVerticalSequences(String[] dna) {
        int n = dna.length;
        int matchCount = 0;

        for (int j = 0; j < n; j++) {
            StringBuilder columnData = new StringBuilder();
            for (int i = 0; i < n; i++) {
                columnData.append(dna[i].charAt(j));
            }
            matchCount += sequenceCounter(columnData.toString());
        }

        return matchCount;
    }

    // busco secuencias en diagonales, izquierda-derecha y derecha-izquierda
    private static int findDiagonalSequences(String[] dna) {
        int size = dna.length;
        int diagonalMatches = 0;

        // diagonales izquierda-derecha
        for (int k = 0; k <= size - MIN_SEQUENCE_LENGTH; k++) {
            StringBuilder primaryDiagonal = new StringBuilder();
            StringBuilder secondaryDiagonal = new StringBuilder();

            for (int i = 0; i < size - k; i++) {
                primaryDiagonal.append(dna[i].charAt(i + k));
                if (k != 0) {
                    secondaryDiagonal.append(dna[i + k].charAt(i));
                }
            }
            diagonalMatches += sequenceCounter(primaryDiagonal.toString());
            if (k != 0) {
                diagonalMatches += sequenceCounter(secondaryDiagonal.toString());
            }
        }

        // diagonales derecha-izquierda
        for (int k = MIN_SEQUENCE_LENGTH - 1; k < size; k++) {
            StringBuilder primaryDiagonal = new StringBuilder();
            StringBuilder secondaryDiagonal = new StringBuilder();

            for (int i = 0; i <= k; i++) {
                primaryDiagonal.append(dna[k - i].charAt(i));
                if (k < size - 1) {
                    secondaryDiagonal.append(dna[size - 1 - i].charAt(size - 1 - k + i));
                }
            }
            diagonalMatches += sequenceCounter(primaryDiagonal.toString());
            if (k < size - 1) {
                diagonalMatches += sequenceCounter(secondaryDiagonal.toString());
            }
        }

        return diagonalMatches;
    }

    // cuento secuencias de letras iguales seguidas
    private static int sequenceCounter(String sequence) {
        int count = 0;
        int streak = 1;

        for (int i = 1; i < sequence.length(); i++) {
            if (sequence.charAt(i) == sequence.charAt(i - 1)) {
                streak++;
                if (streak == MIN_SEQUENCE_LENGTH) {
                    count++;
                }
            } else {
                streak = 1;
            }
        }
        return count;
    }

    // guardo el ADN en la base y devuelvo si es mutante o no
    public boolean saveDnaRecord(String[] dna) {
        String dnaJoined = String.join(",", dna);
        Optional<Dna> existingRecord = dnaRepo.findByDna(dnaJoined);

        if (existingRecord.isPresent()) {
            return existingRecord.get().isMutant();
        }

        boolean isMutant = isMutant(dna);
        Dna dnaEntity = Dna.builder()
                .dna(dnaJoined)
                .isMutant(isMutant)
                .build();
        dnaRepo.save(dnaEntity);

        return isMutant;
    }
}
