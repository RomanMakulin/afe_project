package com.example.expertise.model.checklist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "checklist_fields")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistField {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "checklist_id", nullable = false)
    @JsonBackReference
    private Checklist checklist;

    @Column(name = "field_name", nullable = false)
    private String fieldName; // Название поля, например, "Кадастровый номер", "Адрес объекта"

    public ChecklistField(Checklist checklist, String fieldName) {
        this.checklist = checklist;
        this.fieldName = fieldName;
    }
}