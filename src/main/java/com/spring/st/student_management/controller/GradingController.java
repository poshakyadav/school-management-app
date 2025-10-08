package com.spring.st.student_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.spring.st.student_management.model.Grade;
import com.spring.st.student_management.service.GradeService;
import com.spring.st.student_management.service.StudentService;
import com.spring.st.student_management.service.SubjectService;

@Controller
@RequestMapping("/grades")
public class GradingController {

    @Autowired
    private GradeService gradingService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/list")
    public String listGrades(Model model) {
        model.addAttribute("grades", gradingService.findAll());
        model.addAttribute("activePage", "/grades/list");
        return "grades/list";
    }

    @GetMapping("/add")
    public String showGradeForm(Model model) {
        model.addAttribute("grade", new Grade());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", subjectService.findAll());

        model.addAttribute("activePage", "/grades/add");
        return "grades/form";
    }

    @PostMapping("/add")
    public String saveGrade(@ModelAttribute Grade grade) {
        gradingService.save(grade);
        return "redirect:/grades/list";
    }

    @GetMapping("/edit/{id}")
    public String editGrade(@PathVariable Long id, Model model) {
        model.addAttribute("grade", gradingService.findById(id));
        return "grades/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteGrade(@PathVariable Long id) {
        gradingService.delete(id);
        return "redirect:/grades/list";
    }

    @GetMapping("/report")
    public String generateGradeReport(@RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId,
            Model model) {
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", subjectService.findAll());
        if (studentId != null || courseId != null) {
            model.addAttribute("report", gradingService.findGradesByStudentAndCourse(studentId, courseId));
        }
        return "grades/report";
    }
}
