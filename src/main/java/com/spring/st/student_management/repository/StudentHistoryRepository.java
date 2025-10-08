package com.spring.st.student_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.st.student_management.model.StudentHistory;

@Repository
public interface StudentHistoryRepository extends JpaRepository<StudentHistory, Long> {
}
