package com.spring.st.student_management.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.Teacher;
import com.spring.st.student_management.repository.TeacherRepository;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public List<Teacher> findAll() {
        return teacherRepository.findAll(); // Retrieves all teachers
    }

    public Teacher findById(Long id) {
        return teacherRepository.findById(id).orElse(null); // Retrieves a teacher by ID
    }

    public Teacher save(Teacher teacher) {
        
        return teacherRepository.save(teacher); // Saves or updates a teacher
    }

    public void delete(Long id) {
        teacherRepository.deleteById(id); // Deletes a teacher by ID
    }

   
}
