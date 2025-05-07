package com.example.expertise.repository.checklist;

import com.example.expertise.model.checklist.ChecklistParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью ChecklistParameter.
 */
@Repository
public interface ChecklistParameterRepository extends JpaRepository<ChecklistParameter, UUID> {
}