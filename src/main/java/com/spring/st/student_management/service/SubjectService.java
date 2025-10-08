package com.spring.st.student_management.service;


import com.spring.st.student_management.model.Subject;
import com.spring.st.student_management.repository.SubjectRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    // Find a course by its ID
    public Subject findById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    // Save or update a course
    public Subject save(Subject course) {
        return subjectRepository.save(course);
    }

    // Find a course by its name
    public Subject findByName(String name) {
        return subjectRepository.findByName(name);
    }

    // Retrieve all courses
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    // Delete a course by its ID
    public void delete(Long id) {
        subjectRepository.deleteById(id);
    }
}
