package com.spring.st.student_management.util;


import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailPattern, email);
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }
}
