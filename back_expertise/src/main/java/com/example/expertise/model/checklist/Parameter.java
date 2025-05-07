package com.example.expertise.model.checklist;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Сущность "Параметр"
 * Например "Полы"
 */
@Entity
@Table(name = "parameters")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Parameter {

    /**
     * Уникальный идентификатор параметра
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    /**
     * Название параметра
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Список типов параметра
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "parameter", cascade = CascadeType.ALL)
    private List<ParameterType> parameterTypes;
}