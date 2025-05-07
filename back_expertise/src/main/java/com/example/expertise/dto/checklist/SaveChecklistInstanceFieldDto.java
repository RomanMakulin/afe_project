package com.example.expertise.dto.checklist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SaveChecklistInstanceFieldDto {
    @JsonProperty("checklist_instance_id")
    private UUID checklistInstanceId;

    @JsonProperty("checklist_field_id")
    private UUID checklistFieldId;

    private String value;
}