package com.example.mutant.services;

import com.example.mutant.dto.StatsResponse;
import com.example.mutant.repositories.DnaRepository;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private final DnaRepository dnaRepo;

    public StatsService(DnaRepository dnaRepo) {
        this.dnaRepo = dnaRepo;
    }

    public StatsResponse fetchDnaStatistics() {
        long mutantsCount = dnaRepo.countByIsMutant(true);
        long humansCount = dnaRepo.countByIsMutant(false);

        double ratio = (humansCount == 0) ? 0.0 : (double) mutantsCount / humansCount;
        return new StatsResponse(mutantsCount, humansCount, ratio);
    }
}
