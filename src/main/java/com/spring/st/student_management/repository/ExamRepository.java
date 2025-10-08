package com.spring.st.student_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.st.student_management.model.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByAcademicYearId(Long academicYearId);
}
