package com.example.expertise.model.checklist;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Сущность "Спецификация параметра"
 */
@Entity
@Table(name = "parameter_specs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParameterSpec {

    /**
     * Уникальный идентификатор спецификации
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    /**
     * Название спецификации
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Единица измерения спецификации
     */
    @Column(name = "unit")
    private String unit;

    /**
     * Флаг обязательности спецификации
     */
    @Column(name = "is_required")
    private boolean isRequired = true;
}