package com.safestep.platform.simulation.infrastructure.persistence.jpa.adapters;

import com.safestep.platform.simulation.domain.model.aggregates.MedicalSimulation;
import com.safestep.platform.simulation.domain.repositories.MedicalSimulationRepository;
import com.safestep.platform.simulation.infrastructure.persistence.jpa.assemblers.SimulationPersistenceAssembler;
import com.safestep.platform.simulation.infrastructure.persistence.jpa.repositories.MedicalSimulationPersistenceRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class MedicalSimulationRepositoryImpl implements MedicalSimulationRepository {
    private final MedicalSimulationPersistenceRepository repository;

    public MedicalSimulationRepositoryImpl(MedicalSimulationPersistenceRepository repository) {
        this.repository = repository;
    }

    public List<MedicalSimulation> findAll() {
        return repository.findAll().stream().map(SimulationPersistenceAssembler::toDomain).toList();
    }

    public Optional<MedicalSimulation> findBySlug(String slug) {
        return repository.findBySlug(slug).map(SimulationPersistenceAssembler::toDomain);
    }

    public MedicalSimulation save(MedicalSimulation value) {
        return SimulationPersistenceAssembler
                .toDomain(repository.save(SimulationPersistenceAssembler.toPersistence(value)));
    }

    public boolean existsBySlug(String slug) {
        return repository.existsBySlug(slug);
    }
}
