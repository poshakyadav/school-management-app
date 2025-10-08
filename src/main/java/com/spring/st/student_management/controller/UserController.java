package com.spring.st.student_management.controller;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.st.student_management.model.User;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user, Model model){
        String email = user.getEmail();
        String password = user.getPassword();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        System.out.println("Logged in user--> email -" + email + " password - " + password + " authorities -" + authorities);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/profile")
    public String profileHandler(){

        return "user/profile";
    }

}
