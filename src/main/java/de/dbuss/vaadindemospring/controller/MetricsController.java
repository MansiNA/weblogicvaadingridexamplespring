package de.dbuss.vaadindemospring.controller;

import de.dbuss.vaadindemospring.service.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping
    public ResponseEntity<String> getMetrics() {
        String prometheusMetrics = metricsService.fetchMetrics();
        return ResponseEntity.ok().body(prometheusMetrics);
    }
}
