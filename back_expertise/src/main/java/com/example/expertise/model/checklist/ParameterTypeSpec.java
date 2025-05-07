package com.example.expertise.model.checklist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Сущность "Спецификация у типа параметра"
 */
@Entity
@Table(name = "parameter_type_specs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParameterTypeSpec {

    /**
     * Уникальный идентификатор спецификации у типа параметра
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    /**
     * Тип параметра
     */
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parameter_type_id", nullable = false)
    private ParameterType parameterType;

    /**
     * Спецификация
     */
    @ManyToOne
    @JoinColumn(name = "spec_id", nullable = false)
    private ParameterSpec spec;

    /**
     * Значение по умолчанию для данной спецификации
     */
    @Column(name = "default_value")
    private String defaultValue;
}