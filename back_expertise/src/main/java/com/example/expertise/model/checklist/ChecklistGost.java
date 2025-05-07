package com.example.expertise.model.checklist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Сущность "Госты чек-листа"
 */
@Entity
@Table(name = "checklist_gosts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistGost {

    /**
     * Уникальный идентификатор
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    /**
     * ГОСТ - название
     */
    @Column(nullable = false)
    private String name;

    /**
     * Чек-лист
     */
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;
}
