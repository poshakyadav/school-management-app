package com.spring.st.student_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.Subject;
import com.spring.st.student_management.model.Grade;
import com.spring.st.student_management.model.Student;
import com.spring.st.student_management.repository.GradeRepository;
import com.spring.st.student_management.repository.StudentRepository;
import com.spring.st.student_management.repository.SubjectRepository;

import java.util.List;

@Service
public class GradeService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    // Retrieve all grades
    public List<Grade> findAll() {
        return gradeRepository.findAll();
    }

    // Find grades by student ID
    public List<Grade> findByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    // Save or update a grade
    public Grade save(Grade grade) {
        return gradeRepository.save(grade);
    }

    // Find grade by ID
    public Grade findById(Long id) {
        return gradeRepository.findById(id).orElse(null);
    }

    // Find grades by student and course
    public List<Grade> findGradesByStudentAndCourse(Long studentId, Long courseId) {
        // Fetch the Student and Course entities
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
                Subject course = subjectRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        // Use the repository method with the fetched entities
        return gradeRepository.findByStudentAndSubject(student, course);
    }

    // Delete a grade by ID
    public void delete(Long id) {
        gradeRepository.deleteById(id);
    }
}