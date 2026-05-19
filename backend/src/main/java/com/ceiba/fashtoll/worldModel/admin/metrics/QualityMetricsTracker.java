package com.ceiba.fashtoll.worldModel.admin.metrics;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class QualityMetricsTracker {
    private final AtomicLong successfulRequests = new AtomicLong(0);
    private final AtomicLong failedRequests = new AtomicLong(0);

    public void incrementSuccess() { successfulRequests.incrementAndGet(); }
    public void incrementFailure() { failedRequests.incrementAndGet(); }

    private final List<Double> queryQualities = new CopyOnWriteArrayList<>();

    public void addQueryQualitySample(double sample) {
        this.queryQualities.add(sample);
    }

    public double getAverageQueryQuality() {
        if (queryQualities.isEmpty()) return 0.0;
        return queryQualities.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public long getSuccessfulRequestsCount() {
        return this.successfulRequests.get();
    }

    // Calcula el porcentaje de éxito
    public double getApiRobustnessIndex() {
        long total = successfulRequests.get() + failedRequests.get();
        if (total == 0) return 100.0;
        return (double) successfulRequests.get() / total * 100;
    }
}