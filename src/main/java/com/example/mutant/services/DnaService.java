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

    public boolean detectMutant(String[] dna) {
        validateDnaFormat(dna);

        int sequenceMatches = IntStream.of(
                findHorizontalSequences(dna),
                findVerticalSequences(dna),
                findDiagonalSequences(dna)
        ).parallel().sum();

        return sequenceMatches > 1;
    }

    private void validateDnaFormat(String[] dna) {
        if (dna == null || dna.length == 0 || !IntStream.range(0, dna.length)
                .allMatch(i -> dna[i] != null && dna[i].matches("[ATGC]+") && dna[i].length() == dna.length)) {
            throw new IllegalArgumentException("Invalid DNA format. Only 'A', 'T', 'G', 'C' are allowed in NxN structure.");
        }
    }

    private int findHorizontalSequences(String[] dna) {
        int matchCount = 0;
        for (String row : dna) {
            matchCount += sequenceCounter(row);
        }
        return matchCount;
    }

    private int findVerticalSequences(String[] dna) {
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

    private int findDiagonalSequences(String[] dna) {
        int size = dna.length;
        int diagonalMatches = 0;

        for (int k = 0; k < size - MIN_SEQUENCE_LENGTH + 1; k++) {
            String leftDiagonal = "", rightDiagonal = "";

            for (int i = 0; i < size - k; i++) {
                leftDiagonal += dna[i].charAt(i + k);
                rightDiagonal += dna[i + k].charAt(i);
            }
            diagonalMatches += sequenceCounter(leftDiagonal) + sequenceCounter(rightDiagonal);
        }

        return diagonalMatches;
    }

    private int sequenceCounter(String sequence) {
        int count = 0;
        int streak = 1;

        for (int i = 1; i < sequence.length(); i++) {
            if (sequence.charAt(i) == sequence.charAt(i - 1)) {
                streak++;
                if (streak == MIN_SEQUENCE_LENGTH) {
                    count++;
                }
            } else {
                streak = 1; // reset streak
            }
        }
        return count;
    }

    public boolean saveDnaRecord(String[] dna) {
        String dnaJoined = String.join(",", dna);
        Optional<Dna> existingRecord = dnaRepo.findByDna(dnaJoined);

        if (existingRecord.isPresent()) {
            return existingRecord.get().isMutant();
        }

        boolean isMutant = detectMutant(dna);
        Dna dnaEntity = Dna.builder()
                .dna(dnaJoined)
                .isMutant(isMutant)
                .build();
        dnaRepo.save(dnaEntity);

        return isMutant;
    }
}
