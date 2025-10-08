package com.spring.st.student_management.repository;

import org.springframework.stereotype.Repository;

import com.spring.st.student_management.model.Subject;


import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Subject findByName(String name);
    
}

