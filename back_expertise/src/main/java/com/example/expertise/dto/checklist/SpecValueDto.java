package com.example.expertise.dto.checklist;

import com.example.expertise.model.checklist.SpecValue;
import lombok.Data;

import java.util.UUID;

/**
 * DTO для получения данных значения спецификации чек-листа
 */
@Data
public class SpecValueDto {
    private UUID id;
    private UUID specId;
    private String specName;
    private String value;

    public SpecValueDto(SpecValue specValue) {
        this.id = specValue.getId();
        this.specId = specValue.getSpec().getId();
        this.specName = specValue.getSpec().getName();
        this.value = specValue.getValue();
    }
}