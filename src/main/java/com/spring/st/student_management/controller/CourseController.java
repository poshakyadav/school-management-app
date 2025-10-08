package com.spring.st.student_management.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.st.student_management.model.Subject;
import com.spring.st.student_management.service.SubjectService;
import com.spring.st.student_management.service.TeacherService;


@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private SubjectService courseService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/list")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("activePage", "/courses/list");
        model.addAttribute("activeDropdown", "subject");
        return "courses/list";
    }

    @GetMapping("/add")
    public String addCourseForm(Model model) {
        model.addAttribute("course", new Subject());
        model.addAttribute("teacher", teacherService.findAll());
        model.addAttribute("activePage", "/courses/add");
        model.addAttribute("activeDropdown", "subject");
        return "courses/form";
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute Subject course) {
        courseService.save(course);
        return "redirect:/courses/list";
    }

    @GetMapping("/edit/{id}")
    public String editCourseForm(@PathVariable("id") Long id, Model model) {
        Subject course = courseService.findById(id);
        model.addAttribute("course", course);
        return "courses/form";
    }

    @PostMapping("/edit")
    public String updateCourse(@ModelAttribute Subject course) {
        courseService.save(course);
        return "redirect:/courses/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.delete(id);
        return "redirect:/courses/list";
    }
}
