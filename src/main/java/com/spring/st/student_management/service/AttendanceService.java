package com.spring.st.student_management.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.Attendance;
import com.spring.st.student_management.repository.AttendanceRepository;

import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }

    public void delete(Long id) {
        attendanceRepository.deleteById(id);
    }

    public List<Attendance> findByStudentId(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public Attendance findById(Long id) {
        return attendanceRepository.findById(id).orElse(null);
    }
}

