package com.example.expertise.repository.checklist;

import com.example.expertise.model.checklist.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Checklist (верхний уровень чек-листов).
 */
@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, UUID> {
}