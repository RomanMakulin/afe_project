package com.example.expertise.repository.checklist;

import com.example.expertise.model.checklist.Subtype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Subtype.
 */
@Repository
public interface SubtypeRepository extends JpaRepository<Subtype, UUID> {
}