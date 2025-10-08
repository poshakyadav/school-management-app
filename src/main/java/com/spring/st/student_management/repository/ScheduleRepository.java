package com.spring.st.student_management.repository;


import org.springframework.stereotype.Repository;

import com.spring.st.student_management.model.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}

