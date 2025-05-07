package com.example.expertise.repository.checklist;

import com.example.expertise.model.checklist.ParameterTypeSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью ParameterTypeSpec.
 */
@Repository
public interface ParameterTypeSpecRepository extends JpaRepository<ParameterTypeSpec, UUID> {
}