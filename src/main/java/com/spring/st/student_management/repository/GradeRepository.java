package com.spring.st.student_management.repository;


import org.springframework.stereotype.Repository;

import com.spring.st.student_management.model.Subject;
import com.spring.st.student_management.model.Grade;
import com.spring.st.student_management.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByStudentId(Long studentId);

    List<Grade> findByStudentAndSubject(Student student, Subject course);

}

