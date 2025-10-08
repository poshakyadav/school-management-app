package com.spring.st.student_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.AcademicYear;
import com.spring.st.student_management.repository.AcademicYearRepository;

@Service
public class AcademicYearService {

    @Autowired
    private AcademicYearRepository academicYearRepository;

    public AcademicYear getActiveAcademicYear() {
        return academicYearRepository.findByIsActiveTrue()
                .orElseThrow(() -> new RuntimeException("No active academic year found."));
    }

    public AcademicYear createAcademicYear(AcademicYear academicYear) {
        academicYear.setIsActive(false); // Newly created years are inactive by default
        return academicYearRepository.save(academicYear);
    }

    public void setActiveAcademicYear(Long id) {
        academicYearRepository.findAll().forEach(year -> {
            year.setIsActive(year.getId().equals(id));
            academicYearRepository.save(year);
        });
    }

    // Fetch all academic years
    public List<AcademicYear> getAllAcademicYears() {
        return academicYearRepository.findAll();
    }

}
