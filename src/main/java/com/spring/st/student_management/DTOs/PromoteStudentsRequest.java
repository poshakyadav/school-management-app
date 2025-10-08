package com.spring.st.student_management.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromoteStudentsRequest {
    private List<Long> studentIds; // IDs of students to promote
    private Long nextClassId; // ID of the next class
    private Long nextAcademicYearId; // ID of the next academic year

    // Getters and setters
}