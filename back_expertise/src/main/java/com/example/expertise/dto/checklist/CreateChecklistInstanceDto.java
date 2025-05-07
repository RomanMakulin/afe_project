package com.example.expertise.dto.checklist;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * DTO для создания экземпляра чек-листа
 */
@Data
public class CreateChecklistInstanceDto {

    /**
     * Идентификатор чек-листа (верхний уровень), к которому относится текущий экземпляр
     */
    @NotNull(message = "Checklist ID cannot be null")
    @JsonProperty("checklist_id")
    private UUID checklistId;

    /**
     * Идентификатор параметра, к которому относится текущий экземпляр чек-листа
     */
    @NotNull(message = "Parameter ID cannot be null")
    @JsonProperty("parameter_id")
    private UUID parameterId;

    /**
     * Идентификатор типа параметра, к которому относится текущий экземпляр чек-листа
     */
    @NotNull(message = "Parameter type ID cannot be null")
    @JsonProperty("parameter_type_id")
    private UUID parameterTypeId;

    /**
     * Идентификатор подтипа, к которому относится текущий экземпляр чек-листа. Может быть null
     */
    @JsonProperty("subtype_id")
    private UUID subtypeId;

    /**
     * Идентификатор вопроса экспертизы, к которому относится текущий экземпляр чек-листа
     */
    @NotNull(message = "Expertise question ID cannot be null")
    @JsonProperty("expertise_question_id")
    private UUID expertiseQuestionId;
}