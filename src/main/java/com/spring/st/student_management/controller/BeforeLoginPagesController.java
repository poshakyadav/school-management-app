package com.spring.st.student_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BeforeLoginPagesController {

    @GetMapping("/")
    public String home() {
        return "beforeLoginPages/home";
    }

    @GetMapping("/about")
    public String about() {
        return "beforeLoginPages/about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "beforeLoginPages/contact";
    }
}
