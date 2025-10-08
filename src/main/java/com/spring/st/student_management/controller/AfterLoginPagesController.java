package com.spring.st.student_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AfterLoginPagesController {

    @GetMapping("/dashboard")
    public String home(Model model) {
        model.addAttribute("activePage", "/dashboard");
        model.addAttribute("activeDropdown", "dashboard");
        return "afterLoginPages/dashboard";
    }
}
