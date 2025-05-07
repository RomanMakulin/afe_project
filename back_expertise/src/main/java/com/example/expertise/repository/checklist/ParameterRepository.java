package com.example.expertise.repository.checklist;

import com.example.expertise.model.checklist.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Parameter (бывший Checklist).
 */
@Repository
public interface ParameterRepository extends JpaRepository<Parameter, UUID> {
}