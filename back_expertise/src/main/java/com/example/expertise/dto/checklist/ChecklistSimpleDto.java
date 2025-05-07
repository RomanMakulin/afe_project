package com.example.expertise.dto.checklist;

import com.example.expertise.model.checklist.Checklist;
import lombok.Data;

import java.util.UUID;

@Data
public class ChecklistSimpleDto {
    private UUID id;
    private String name;

    public ChecklistSimpleDto(Checklist checklist) {
        this.id = checklist.getId();
        this.name = checklist.getName();
    }
}