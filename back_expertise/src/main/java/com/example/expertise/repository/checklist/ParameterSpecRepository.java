package com.example.expertise.repository.checklist;

import com.example.expertise.model.checklist.ParameterSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью ParameterSpec (бывший Parameter).
 */
@Repository
public interface ParameterSpecRepository extends JpaRepository<ParameterSpec, UUID> {
}