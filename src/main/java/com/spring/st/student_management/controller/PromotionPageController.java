package com.spring.st.student_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.st.student_management.model.AcademicYear;
import com.spring.st.student_management.model.Student;
import com.spring.st.student_management.model.StudentClass;
import com.spring.st.student_management.repository.AcademicYearRepository;
import com.spring.st.student_management.repository.StudentClassRepository;
import com.spring.st.student_management.repository.StudentRepository;

@Controller
@RequestMapping("/students/")
public class PromotionPageController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @GetMapping("/promotion")
    public String showPromotionPage(Model model) {
        // Fetch all active students for the current academic year
        AcademicYear activeYear = academicYearRepository.findByIsActive(true);
        List<Student> students = studentRepository.findByAcademicYear(activeYear);

        // Fetch all available classes and academic years
        List<StudentClass> classes = studentClassRepository.findAll();
        List<AcademicYear> academicYears = academicYearRepository.findAll();

        model.addAttribute("students", students);
        model.addAttribute("classes", classes);
        model.addAttribute("academicYears", academicYears);

        model.addAttribute("activePage", "/promotion");
        model.addAttribute("activeDropdown", "students");

        return "promotion/promotion";
    }
}
