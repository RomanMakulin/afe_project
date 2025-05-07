package com.example.expertise.dto.checklist;

import com.example.expertise.model.checklist.Parameter;
import lombok.Data;

import java.util.UUID;

@Data
public class ParameterSimpleDto {
    private UUID id;
    private String name;

    public ParameterSimpleDto(Parameter parameter) {
        this.id = parameter.getId();
        this.name = parameter.getName();
    }
}