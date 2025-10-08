package com.spring.st.student_management.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_classes")
public class StudentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "Class 10"

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "class_section_mapping", joinColumns = @JoinColumn(name = "student_class_id"), inverseJoinColumns = @JoinColumn(name = "section_id"))
    private List<Section> sections;

    private String roomNumber; // Class Room Number

    private String description; // Class Description

    @ManyToOne
    @JoinColumn(name = "next_class_id")
    private StudentClass nextClass; // Self-referencing for promotion

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "class_fee_structure_mapping", joinColumns = @JoinColumn(name = "class_id"), inverseJoinColumns = @JoinColumn(name = "fee_structure_id"))
    private List<FeeStructure> feeStructures;

    @ManyToOne
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear; // class academic Year, like- 2024 - 2025

    @Override
    public String toString() {
        String feeStructureDetails = (feeStructures != null && !feeStructures.isEmpty())
                ? feeStructures.toString()
                : "No Fee Structures";

        return "StudentClass [id=" + id + ", name=" + name + ", roomNumber=" + roomNumber
                + ", description=" + description + ", nextClass=" + (nextClass != null ? nextClass.getId() : "null")
                + ", feeStructures=" + feeStructureDetails
                + ", academicYear=" + academicYear + "]";
    }

    // Add other fields like teacher, timings, etc., if required

}
