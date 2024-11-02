package com.example.mutant.controllers;

import com.example.mutant.dto.StatsResponse;
import com.example.mutant.services.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StatsControllerTest {

    private StatsService statsService;
    private StatsController statsController;

    @BeforeEach
    void setUp() {
        statsService = Mockito.mock(StatsService.class);
        statsController = new StatsController(statsService);
    }

    @Test
    void returnsCorrectStats() {
        StatsResponse mockResponse = new StatsResponse(2, 2, 1.0);
        when(statsService.fetchDnaStatistics()).thenReturn(mockResponse);
        StatsResponse response = statsController.getStats();
        assertEquals(2, response.getCountMutantDna());
        assertEquals(2, response.getCountHumanDna());
        assertEquals(1.0, response.getRatio());
    }
}
