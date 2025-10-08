package com.spring.st.student_management.model;

import java.time.LocalDate;
import java.util.ArrayList;
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
import jakarta.persistence.OneToMany;
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
@Table(name = "students")
public class Student extends User {

    public Student(Long studentId) {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private StudentClass studentClass;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    private LocalDate dateOfBirth; // Student date of brith

    private LocalDate dateOfAdmission; // date of admittion

    private String rollNumber; // Student Roll Number

    @ManyToOne
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear; // Student Current Academic Year

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Subject subject;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeeAllocation> feeAllocations; // Student Assigned Fee

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments; // Student makes payment

    @ManyToMany
    @JoinTable(name = "student_fee_structure", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "fee_structure_id"))
    private List<FeeStructure> feeStructures = new ArrayList<>(); // Student Assigned Fee Structure

    private Double outstandingBalance; // Student Total Outstanding Balance

    private Double totalPayments; // Total Payments make by student for submit fee

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentHistory> studentHistories = new ArrayList<>();

    @Override
    public String toString() {
        return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", studentClass="
                + studentClass + ", section=" + section + ", dateOfBirth=" + dateOfBirth + ", dateOfAdmission="
                + dateOfAdmission + ", rollNumber=" + rollNumber + ", academicYear=" + academicYear + ", subject="
                + subject + ", feeAllocations=" + feeAllocations + ", payments=" + payments + ", feeStructures="
                + feeStructures + ", outstandingBalance=" + outstandingBalance + ", totalPayments=" + totalPayments
                + "]";
    }

    // Getters and setters

}
