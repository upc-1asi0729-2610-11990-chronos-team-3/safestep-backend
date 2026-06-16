package com.safestep.platform.simulation.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import java.util.List;

public record CreateAttemptResource(String mode, Instant startedAt, Instant completedAt, @Min(0) @Max(100) int score,
        @Min(0) int totalSteps, @Min(0) int correctSteps, @Min(0) long timeElapsed,
        List<AttemptErrorResource> errors) {
}
