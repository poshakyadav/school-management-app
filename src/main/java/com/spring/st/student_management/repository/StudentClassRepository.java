package com.spring.st.student_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.st.student_management.model.StudentClass;

public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {
    // JpaRepository already provides methods like findAll, save, deleteById, etc.

    @Query("SELECT c FROM StudentClass c JOIN FETCH c.sections")
    List<StudentClass> findAllWithSections();
}
