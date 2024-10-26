package com.example.mutant.controllers;

import com.example.mutant.dto.StatsResponse;
import com.example.mutant.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService service) {
        this.statsService = service;
    }

    @GetMapping
    public StatsResponse getStats() {
        return statsService.fetchDnaStatistics();
    }
}
