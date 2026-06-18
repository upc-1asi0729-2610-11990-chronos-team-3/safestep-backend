package com.safestep.platform.simulation.interfaces.rest.resources;

public record AttemptErrorResource(int stepNumber, String error, String severity) {
}
