package com.example.expertise.repository.checklist;

import com.example.expertise.model.checklist.ParameterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью ParameterType (бывший ChecklistType).
 */
@Repository
public interface ParameterTypeRepository extends JpaRepository<ParameterType, UUID> {
}