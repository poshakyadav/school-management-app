package com.spring.st.student_management.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String message;
    
    @Enumerated(EnumType.STRING)
    private InquiryType inquiryType;

    @Enumerated(EnumType.STRING)
    private ContactMethod contactMethod;

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean replied = false;

    private boolean hidden;


    public enum InquiryType {
        ADMISSION, BECOME_TEACHER, GENERAL_QUERY
    }

    public enum ContactMethod {
        EMAIL, PHONE, BOTH
    }
}
