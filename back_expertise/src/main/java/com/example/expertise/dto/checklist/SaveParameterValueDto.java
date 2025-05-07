package com.example.expertise.dto.checklist;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * DTO для сохранения значения спецификации чек-листа
 */
@Data
public class SaveParameterValueDto {

    /**
     * Идентификатор экземпляра чек-листа, к которому относится текущее значение
     */
    @NotNull(message = "Checklist instance ID cannot be null")
    @JsonProperty("checklist_instance_id")
    private UUID checklistInstanceId;

    /**
     * Идентификатор спецификации, к которой относится текущее значение
     */
    @NotNull(message = "Spec ID cannot be null")
    @JsonProperty("spec_id")
    private UUID specId;

    /**
     * Значение спецификации
     */
    @NotNull(message = "Value cannot be null")
    private String value;
}