package com.example.expertise.dto.checklist;

import com.example.expertise.model.checklist.Subtype;
import lombok.Data;

import java.util.UUID;

@Data
public class SubtypeSimpleDto {
    private UUID id;
    private String name;

    public SubtypeSimpleDto(Subtype subtype) {
        this.id = subtype.getId();
        this.name = subtype.getName();
    }
}