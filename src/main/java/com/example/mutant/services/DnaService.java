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

    // revisa si el adn tiene más de una secuencia de cuatro letras iguales
    public static boolean isMutant(String[] dna) {
        validateDnaFormat(dna);

        // suma todas las secuencias que encuentre horizontal, vertical y diagonal
        int sequenceMatches = IntStream.of(
                findHorizontalSequences(dna),
                findVerticalSequences(dna),
                findDiagonalSequences(dna)
        ).parallel().sum();

        return sequenceMatches > 1; // si encuentra más de una secuencia, es mutante
    }

    // asegura que el adn es cuadrado y solo contiene caracteres A, T, G o C
    private static void validateDnaFormat(String[] dna) {
        if (dna == null || dna.length == 0 || !IntStream.range(0, dna.length)
                .allMatch(i -> dna[i] != null && dna[i].matches("[ATGC]+") && dna[i].length() == dna.length)) {
            throw new IllegalArgumentException("formato de adn no válido, solo A, T, G y C en estructura NxN");
        }
    }

    // busca secuencias en cada fila
    private static int findHorizontalSequences(String[] dna) {
        int matchCount = 0;
        for (String row : dna) {
            matchCount += sequenceCounter(row);
        }
        return matchCount;
    }

    // busca secuencias en cada columna
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

    // busca secuencias en diagonales de izquierda a derecha
    private static int findDiagonalSequences(String[] dna) {
        int size = dna.length;
        int diagonalMatches = 0;

        // buscar diagonales de izquierda a derecha
        for (int i = 0; i < size; i++) {
            StringBuilder leftDiagonal = new StringBuilder();
            StringBuilder rightDiagonal = new StringBuilder();

            for (int j = 0; j < size - i; j++) {
                leftDiagonal.append(dna[j].charAt(i + j)); // diagonal hacia abajo
                rightDiagonal.append(dna[i + j].charAt(j)); // diagonal hacia arriba
            }
            diagonalMatches += sequenceCounter(leftDiagonal.toString());
            if (i > 0) {
                diagonalMatches += sequenceCounter(rightDiagonal.toString());
            }
        }

        // buscar diagonales de derecha a izquierda
        for (int i = 0; i < size; i++) {
            StringBuilder leftDiagonal = new StringBuilder();
            StringBuilder rightDiagonal = new StringBuilder();

            for (int j = 0; j <= i && j < size; j++) {
                leftDiagonal.append(dna[j].charAt(size - 1 - i + j)); // diagonal hacia abajo
                rightDiagonal.append(dna[i - j].charAt(size - 1 - j)); // diagonal hacia arriba
            }
            diagonalMatches += sequenceCounter(leftDiagonal.toString());
            if (i > 0) {
                diagonalMatches += sequenceCounter(rightDiagonal.toString());
            }
        }

        return diagonalMatches;
    }

    // cuenta secuencias de cuatro letras iguales seguidas en la cadena que le pases
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
                streak = 1; // resetea la racha si la secuencia se corta
            }
        }
        return count;
    }

    // guarda el adn y devuelve si es mutante o no
    public boolean saveDnaRecord(String[] dna) {
        String dnaJoined = String.join(",", dna);
        Optional<Dna> existingRecord = dnaRepo.findByDna(dnaJoined);

        // si ya existe, devuelve el resultado guardado para no analizarlo de nuevo
        if (existingRecord.isPresent()) {
            return existingRecord.get().isMutant();
        }

        boolean isMutant = isMutant(dna); // Cambiado a isMutant
        Dna dnaEntity = Dna.builder()
                .dna(dnaJoined)
                .isMutant(isMutant)
                .build();
        dnaRepo.save(dnaEntity);

        return isMutant;
    }
}