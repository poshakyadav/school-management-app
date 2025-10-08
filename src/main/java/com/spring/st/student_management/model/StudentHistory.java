package com.spring.st.student_management.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class StudentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private StudentClass previousClass;

    @ManyToOne
    @JoinColumn(name = "academic_year_id")
    private AcademicYear previousAcademicYear;

    private String grades; // E.g., "A+", "B"

    private Integer totalMarks;

    private Double totalFeePaid;
    private Double totalAssignedFee;

    private String remarks; // Optional comments

    @CreationTimestamp
    private LocalDateTime createdAt;




}
