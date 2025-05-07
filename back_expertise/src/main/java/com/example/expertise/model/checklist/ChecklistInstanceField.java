package com.example.expertise.model.checklist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "checklist_instance_fields")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistInstanceField {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "checklist_instance_id", nullable = false)
    @JsonBackReference
    private ChecklistInstance checklistInstance;

    @ManyToOne
    @JoinColumn(name = "checklist_field_id", nullable = false)
    private ChecklistField checklistField;

    @Column(name = "value")
    private String value; // Значение поля, например, "12:34:567890:1"

    public ChecklistInstanceField(ChecklistInstance checklistInstance, ChecklistField checklistField, String value) {
        this.checklistInstance = checklistInstance;
        this.checklistField = checklistField;
        this.value = value;
    }
}