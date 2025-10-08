package com.spring.st.student_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.st.student_management.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Section findAllById(Long[] sectionIds);
}
