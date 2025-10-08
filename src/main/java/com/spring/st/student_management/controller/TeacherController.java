package com.spring.st.student_management.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.st.student_management.model.Subject;
import com.spring.st.student_management.model.Teacher;
import com.spring.st.student_management.service.SubjectService;
import com.spring.st.student_management.service.TeacherService;


@Controller
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/list")
    public String listTeachers(Model model) {
        model.addAttribute("teachers", teacherService.findAll());

        model.addAttribute("activePage", "/teachers/list");

        model.addAttribute("activeDropdown", "teachers");
        return "teachers/list";
    }

    @GetMapping("/add")
    public String addTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("subject", subjectService.findAll());
        model.addAttribute("activePage", "/teachers/add");
        model.addAttribute("activeDropdown", "teachers");
        return "teachers/form";
    }

    @PostMapping("/add")
    public String addTeacher(@ModelAttribute Teacher teacher, 
                             @RequestParam(name = "subject", required = false) Long subjectId) {
        if (subjectId != null) {
            // If a subject is selected, fetch it and set it to the teacher
            Subject subject = subjectService.findById(subjectId);
            teacher.setSubject(subject);
        } else {
            // If no subject is selected, set subject as null
            teacher.setSubject(null);
        }
        
        teacherService.save(teacher);
        return "redirect:/teachers/list";
    }


    @GetMapping("/edit/{id}")
    public String editTeacherForm(@PathVariable("id") Long id, Model model) {
        Teacher teacher = teacherService.findById(id);
        model.addAttribute("teacher", teacher);
        return "teachers/form";
    }

    @PostMapping("/edit")
    public String updateTeacher(@ModelAttribute Teacher teacher) {
        teacherService.save(teacher);
        return "redirect:/teachers/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable("id") Long id) {
        teacherService.delete(id);
        return "redirect:/teachers/list";
    }
}
