package com.example.expertise.repository.checklist;

import com.example.expertise.model.checklist.ChecklistInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью ChecklistInstance.
 */
@Repository
public interface ChecklistInstanceRepository extends JpaRepository<ChecklistInstance, UUID> {
    // Пример кастомного запроса: найти все экземпляры чек-листов по вопросу экспертизы
    List<ChecklistInstance> findByExpertiseQuestionId(UUID expertiseQuestionId);
}