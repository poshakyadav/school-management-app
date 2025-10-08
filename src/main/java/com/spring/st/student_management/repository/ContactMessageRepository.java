package com.spring.st.student_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.st.student_management.model.ContactMessage;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    List<ContactMessage> findByRepliedFalse();  // Fetch messages that haven't been replied to yet

    List<ContactMessage> findByHiddenFalse(); // Fetch only visible messages
}