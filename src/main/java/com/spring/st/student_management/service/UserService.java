package com.spring.st.student_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.User;
import com.spring.st.student_management.repository.UserRepository;
import com.spring.st.student_management.util.RoleConstants;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Save user with encoded password
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(RoleConstants.ROLE_ADMIN)); //Set admin role here. this for testing
        userRepository.save(user);
    }

}