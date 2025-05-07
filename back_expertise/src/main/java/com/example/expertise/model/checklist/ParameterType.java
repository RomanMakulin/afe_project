package com.example.expertise.model.checklist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Сущность "Тип параметра"
 * Например "Ленточные фундаменты"
 */
@Entity
@Table(name = "parameter_types")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParameterType {

    /**
     * Уникальный идентификатор типа параметра
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    /**
     * Название типа параметра
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Связь с параметром
     */
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parameter_id", nullable = false)
    private Parameter parameter;

    /**
     * Список подтипов у текущего типа параметра
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "parameterType", cascade = CascadeType.ALL)
    private List<Subtype> subtypes;

    /**
     * Список спецификаций типа параметра
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "parameterType", cascade = CascadeType.ALL)
    private List<ParameterTypeSpec> parameterTypeSpecs;
}