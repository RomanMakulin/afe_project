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
 * Сущность "Чек-лист"
 */
@Entity
@Table(name = "checklists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Checklist {

    /**
     * Уникальный идентификатор чек-листа
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    /**
     * Название чек-листа
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Описание чек-листа
     */
    @Column(name = "description")
    private String description;

    /**
     * Список ГОСТов, связанных с чек-листом
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL)
    private List<ChecklistGost> checklistGosts;

    /**
     * Список параметров, связанных с чек-листом
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL)
    private List<ChecklistParameter> checklistParameters;

    @JsonManagedReference
    @OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL)
    private List<ChecklistField> checklistFields;
}