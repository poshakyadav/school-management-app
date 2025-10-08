package com.spring.st.student_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.st.student_management.model.AcademicYear;
import com.spring.st.student_management.service.AcademicYearService;


@Controller
@RequestMapping("/academic-year")
public class AcademicYearController {
    @Autowired
    private AcademicYearService academicYearService;

    @GetMapping("/list")
    public String listAcademicYears(Model model) {
        model.addAttribute("academicYears", academicYearService.getAllAcademicYears());
        return "academic-year/list-academic-years";
    }

    @GetMapping("/create")
    public String createAcademicYearForm(Model model) {
        model.addAttribute("academicYear", new AcademicYear());
        return "academic-year/create-academic-year";
    }

    @PostMapping("/create")
    public String createAcademicYear(@ModelAttribute AcademicYear academicYear) {
        academicYearService.createAcademicYear(academicYear);
        return "redirect:/academic-year/list";
    }

    @PostMapping("/activate/{id}")
    public String activateAcademicYear(@PathVariable Long id) {
        academicYearService.setActiveAcademicYear(id);
        return "redirect:/academic-year/list";
    }
}
