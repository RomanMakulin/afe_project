package com.example.expertise.repository.checklist;

import com.example.expertise.model.checklist.ChecklistField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChecklistFieldRepository extends JpaRepository<ChecklistField, UUID> {
}
