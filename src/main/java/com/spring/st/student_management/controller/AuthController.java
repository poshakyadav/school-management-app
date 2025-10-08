package com.spring.st.student_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.st.student_management.model.User;
import com.spring.st.student_management.service.UserService;

@Controller
public class AuthController {

    
    @Autowired
    private UserService userService;

    @GetMapping("/user-login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        System.out.println("showLoginForm handler..........................................");
        return "auth/login";
    }
    @PostMapping("/user-login")
    public String showLoginFormPost(Model model) {
        model.addAttribute("user", new User());
        System.out.println("showLoginForm handler..........................................");
        return "auth/login";
    }
    @GetMapping("/sign-up")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        System.out.println("calling showRegistrationForm --------------------------");
        return "auth/register";
    }

    @PostMapping("/sign-up")
    public String registerUser(@ModelAttribute User user) {
        userService.save(user);
        System.out.println("calling registerUser -----------------------------------");
        return "redirect:/user-login";
    }

}
