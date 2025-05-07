package com.example.expertise.repository.checklist;

import com.example.expertise.model.checklist.ChecklistInstanceField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChecklistInstanceFieldRepository extends JpaRepository<ChecklistInstanceField, UUID> {
}
