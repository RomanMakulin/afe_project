package com.example.expertise.dto.checklist;

import com.example.expertise.model.checklist.ChecklistInstance;
import com.example.expertise.model.checklist.SpecValue;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO для возврата данных о конкретном экземпляре чек-листа
 */
@Data
public class ChecklistInstanceResponseDto {
    private UUID id;
    private UUID checklistId;
    private String checklistName;
    private UUID parameterId;
    private String parameterName;
    private UUID parameterTypeId;
    private String parameterTypeName;
    private UUID subtypeId;
    private String subtypeName;
    private UUID expertiseQuestionId;
    private LocalDateTime createdAt;
    private List<SpecValueDto> specValues;

    public ChecklistInstanceResponseDto(ChecklistInstance instance) {
        this.id = instance.getId();
        this.checklistId = instance.getChecklist().getId();
        this.checklistName = instance.getChecklist().getName();
        this.parameterId = instance.getParameter().getId();
        this.parameterName = instance.getParameter().getName();
        this.parameterTypeId = instance.getParameterType().getId();
        this.parameterTypeName = instance.getParameterType().getName();
        this.subtypeId = instance.getSubtype() != null ? instance.getSubtype().getId() : null;
        this.subtypeName = instance.getSubtype() != null ? instance.getSubtype().getName() : null;
        this.expertiseQuestionId = instance.getExpertiseQuestion() != null ? instance.getExpertiseQuestion().getId() : null;
        this.createdAt = instance.getCreatedAt();
        this.specValues = instance.getSpecValues() != null ?
                instance.getSpecValues().stream().map(SpecValueDto::new).toList() : null;
    }
}