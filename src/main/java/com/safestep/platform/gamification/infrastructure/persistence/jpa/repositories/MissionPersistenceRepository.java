package com.safestep.platform.gamification.infrastructure.persistence.jpa.repositories;

import com.safestep.platform.gamification.infrastructure.persistence.jpa.entities.MissionPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionPersistenceRepository extends JpaRepository<MissionPersistenceEntity, Long> {
    boolean existsByExternalId(String id);
}
