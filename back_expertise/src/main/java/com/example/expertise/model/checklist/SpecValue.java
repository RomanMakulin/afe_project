package com.example.expertise.model.checklist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Сущность "Значение спецификации"
 */
@Entity
@Table(name = "spec_values")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecValue {

    /**
     * Уникальный идентификатор значения спецификации
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    /**
     * Экземпляр чек-листа, в котором находится данная спецификация
     */
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "checklist_instance_id", nullable = false)
    private ChecklistInstance checklistInstance;

    /**
     * Связь с сущностью спецификации
     */
    @ManyToOne
    @JoinColumn(name = "spec_id", nullable = false)
    private ParameterSpec spec;

    /**
     * Значение спецификации
     */
    @Column(name = "value")
    private String value;

    public SpecValue(ParameterSpec spec, String value) {
        this.spec = spec;
        this.value = value;
    }
}