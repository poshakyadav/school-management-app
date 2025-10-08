package com.spring.st.student_management.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AcademicYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;  // Start Date: 1st April 2024
    private LocalDate endDate; // End Date: 31st March 2025

    private Boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Calculated property for name
    public String getName() {
        return this.startDate.getYear() + " - " + this.endDate.getYear();
    }

    @Override
    public String toString() {
        return "AcademicYear [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", isActive=" + isActive
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

    // Getters and Setters

    
}

