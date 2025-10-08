package com.spring.st.student_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.StudentClass;
import com.spring.st.student_management.repository.StudentClassRepository;

import jakarta.transaction.Transactional;

@Service
public class StudentClassService {

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Transactional
    public List<StudentClass> getAllClassesWithSections() {
        // Ensures sections are fetched eagerly
        return studentClassRepository.findAllWithSections();
    }

    public List<StudentClass> getAllClasses() {
        return studentClassRepository.findAll();
    }

    public List<StudentClass> findAll() {
        return studentClassRepository.findAll();
    }

    public StudentClass getClassById(Long id) {
        return studentClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found"));
    }

    public StudentClass findById(Long id) {
        return studentClassRepository.findById(id).orElse(null); 
    }

    public StudentClass saveClass(StudentClass studentClass) {
        return studentClassRepository.save(studentClass);
    }

    @Transactional
    public void saveClassWithSections(StudentClass studentClass, Long[] sectionIds) {
        studentClassRepository.save(studentClass);
    }

    public void deleteClass(Long id) {
        studentClassRepository.deleteById(id);
    }

}
