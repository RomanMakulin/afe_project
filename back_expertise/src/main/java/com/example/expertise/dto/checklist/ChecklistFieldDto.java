package com.example.expertise.dto.checklist;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChecklistFieldDto {
    private UUID id;
    private String fieldName;

    public ChecklistFieldDto(com.example.expertise.model.checklist.ChecklistField field) {
        this.id = field.getId();
        this.fieldName = field.getFieldName();
    }
}