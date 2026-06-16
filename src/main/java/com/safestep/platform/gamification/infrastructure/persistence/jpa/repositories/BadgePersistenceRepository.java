package com.safestep.platform.gamification.infrastructure.persistence.jpa.repositories;

import com.safestep.platform.gamification.infrastructure.persistence.jpa.entities.BadgePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgePersistenceRepository extends JpaRepository<BadgePersistenceEntity, Long> {
    boolean existsByExternalId(String id);
}
