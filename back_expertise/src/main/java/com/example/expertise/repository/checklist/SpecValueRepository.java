package com.example.expertise.repository.checklist;

import com.example.expertise.model.checklist.SpecValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью SpecValue (бывший ParameterValue).
 */
@Repository
public interface SpecValueRepository extends JpaRepository<SpecValue, UUID> {
}