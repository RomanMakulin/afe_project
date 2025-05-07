package com.example.expertise.model.checklist;

import com.example.expertise.model.expertise.ExpertiseQuestion;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Сущность "Экземпляр чек-листа"
 */
@Entity
@Table(name = "checklist_instances")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistInstance {

    /**
     * Уникальный идентификатор экземпляра чек-листа
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    /**
     * Чек-лист (верхний уровень)
     */
    @ManyToOne
    @JoinColumn(name = "checklist_id", nullable = false)
    private Checklist checklist;

    /**
     * Параметр, к которому относится текущий экземпляр
     */
    @ManyToOne
    @JoinColumn(name = "parameter_id", nullable = false)
    private Parameter parameter;

    /**
     * Тип параметра
     */
    @ManyToOne
    @JoinColumn(name = "parameter_type_id", nullable = false)
    private ParameterType parameterType;

    /**
     * Подтип у типа параметра
     */
    @ManyToOne
    @JoinColumn(name = "subtype_id")
    private Subtype subtype;

    /**
     * Вопрос экспертизы, к которому относится текущий экземпляр чек-листа
     */
    @ManyToOne
    @JoinColumn(name = "expertise_question_id")
    @JsonBackReference
    private ExpertiseQuestion expertiseQuestion;

    /**
     * Дата и время создания экземпляра чек-листа
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Значения спецификаций
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "checklistInstance", cascade = CascadeType.ALL)
    private List<SpecValue> specValues;

    @JsonManagedReference
    @OneToMany(mappedBy = "checklistInstance", cascade = CascadeType.ALL)
    private List<ChecklistInstanceField> checklistInstanceFields;

    public ChecklistInstance(Checklist checklist, Parameter parameter, ParameterType parameterType, Subtype subtype) {
        this.checklist = checklist;
        this.parameter = parameter;
        this.parameterType = parameterType;
        this.subtype = subtype;
    }
}