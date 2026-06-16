package com.safestep.platform.profiles.domain.model.queries;

/**
 * Get my profile query.
 * <p>
 * This query represents the request to retrieve the authenticated user's profile.
 * </p>
 */
public record GetMyProfileQuery(Long userId) {
}