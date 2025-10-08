package com.spring.st.student_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.ContactMessage;
import com.spring.st.student_management.repository.ContactMessageRepository;

@Service
public class ContactMessageService {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    public ContactMessage saveMessage(ContactMessage contactMessage) {
        return contactMessageRepository.save(contactMessage);
    }

    public List<ContactMessage> getAllMessages() {
        return contactMessageRepository.findAll();
    }

    public List<ContactMessage> getUnrepliedMessages() {
        return contactMessageRepository.findByRepliedFalse();
    }

    public Optional<ContactMessage> getMessageById(Long id) {
        return contactMessageRepository.findById(id);
    }

    public void resolveMessage(Long id) {
        Optional<ContactMessage> messageOpt = contactMessageRepository.findById(id);
        if (messageOpt.isPresent()) {
            ContactMessage message = messageOpt.get();
            message.setReplied(true);
            contactMessageRepository.save(message);
        }
    }

    public void hideMessageFromDashboard(Long id) {
        Optional<ContactMessage> messageOpt = contactMessageRepository.findById(id);
        if (messageOpt.isPresent()) {
            ContactMessage message = messageOpt.get();
            message.setHidden(true); // Mark the message as hidden
            contactMessageRepository.save(message);
        }
    }
    
    public void deleteMessage(Long id) {
        contactMessageRepository.deleteById(id); // Permanently delete the message
    }

    public List<ContactMessage> getVisibleMessages() {
        return contactMessageRepository.findByHiddenFalse();
    }
    
    
    
}