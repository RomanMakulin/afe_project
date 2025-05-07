package com.example.expertise.dto.checklist;

import com.example.expertise.model.checklist.ParameterType;
import lombok.Data;

import java.util.UUID;

@Data
public class ParameterTypeSimpleDto {
    private UUID id;
    private String name;

    public ParameterTypeSimpleDto(ParameterType parameterType) {
        this.id = parameterType.getId();
        this.name = parameterType.getName();
    }
}