package com.example.expertise.model.checklist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Сущность "Связь чек-листа и параметра"
 */
@Entity
@Table(name = "checklist_parameters")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistParameter {

    /**
     * Уникальный идентификатор связи
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    /**
     * Чек-лист
     */
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "checklist_id", nullable = false)
    private Checklist checklist;

    /**
     * Параметр
     */
    @ManyToOne
    @JoinColumn(name = "parameter_id", nullable = false)
    private Parameter parameter;
}