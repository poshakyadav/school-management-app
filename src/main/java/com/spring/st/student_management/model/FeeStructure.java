package com.spring.st.student_management.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FeeStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long classId;
    private String feeType; // name of fee structure
    private double amount;
    private String description;
    private String dueDate; // Format: yyyy-MM-dd
    private String status; // Active, Inactive, etc.

    @ManyToMany(mappedBy = "feeStructures")
    private List<StudentClass> assignedClasses = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "student_fee_structure", joinColumns = @JoinColumn(name = "fee_structure_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @Override
    public String toString() {
        String assignedClassDetails = (assignedClasses != null && !assignedClasses.isEmpty())
                ? "Assigned to " + assignedClasses.size() + " classes"
                : "No assigned classes";

        String studentDetails = (students != null && !students.isEmpty())
                ? "Assigned to " + students.size() + " students"
                : "No assigned students";

        return "FeeStructure [id=" + id + ", classId=" + classId + ", feeType=" + feeType + ", amount=" + amount
                + ", description=" + description + ", dueDate=" + dueDate + ", status=" + status
                + ", assignedClasses=" + assignedClassDetails
                + ", students=" + studentDetails
                + ", academicYear=" + (academicYear != null ? academicYear.getId() : "null") + "]";
    }

    // Getters and Setters

    

}