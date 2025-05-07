package com.example.expertise.model.checklist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Сущность "Подтип у типа параметра"
 * Например "Бетон"
 */
@Entity
@Table(name = "subtypes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subtype {

    /**
     * Уникальный идентификатор подтипа
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    /**
     * Название подтипа
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Связь с типом параметра
     */
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parameter_type_id", nullable = false)
    private ParameterType parameterType;
}