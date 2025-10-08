package com.spring.st.student_management.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.spring.st.student_management.model.Schedule;
import com.spring.st.student_management.service.ScheduleService;
import com.spring.st.student_management.service.SubjectService;
import com.spring.st.student_management.service.TeacherService;


@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/list")
    public String listSchedules(Model model) {
        model.addAttribute("schedules", scheduleService.findAll());
        return "schedules/list";
    }

    @GetMapping("/add")
    public String showScheduleForm(Model model) {
        model.addAttribute("schedule", new Schedule());
        model.addAttribute("courses", subjectService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        return "schedules/form";
    }

    @PostMapping("/add")
    public String saveSchedule(@ModelAttribute Schedule schedule) {
        scheduleService.save(schedule);
        return "redirect:/schedules/list";
    }

    @GetMapping("/edit/{id}")
    public String editSchedule(@PathVariable Long id, Model model) {
        Schedule schedule = scheduleService.findById(id);
        model.addAttribute("schedule", schedule);
        model.addAttribute("courses", subjectService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        return "schedules/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return "redirect:/schedules/list";
    }
}
