package com.spring.st.student_management.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.spring.st.student_management.model.Attendance;
import com.spring.st.student_management.service.AttendanceService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/list")
    public String listAttendance(Model model) {
        model.addAttribute("attendanceList", attendanceService.findAll());
        return "attendance/list";
    }

    @GetMapping("/add")
    public String showAttendanceForm(Model model) {
        model.addAttribute("attendance", new Attendance());
        return "attendance/form";
    }

    @PostMapping("/add")
    public String saveAttendance(@ModelAttribute Attendance attendance) {
        attendanceService.save(attendance);
        return "redirect:/attendance/list";
    }

    @GetMapping("/edit/{id}")
    public String editAttendance(@PathVariable Long id, Model model) {
        model.addAttribute("attendance", attendanceService.findById(id));
        return "attendance/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable Long id) {
        attendanceService.delete(id);
        return "redirect:/attendance/list";
    }
}

