package com.spring.st.student_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.st.student_management.model.ContactMessage;
import com.spring.st.student_management.service.ContactMessageService;

@Controller
@RequestMapping("/contact")
public class ContactMessageController {

    @Autowired
    private ContactMessageService contactMessageService;

    @PostMapping("/submitContactForm")
    public String submitContactForm(
            @RequestParam("first-name") String firstName,
            @RequestParam("last-name") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phone-number") String phoneNumber,
            @RequestParam("message") String message,
            @RequestParam("inquiry-type") String inquiryType,
            @RequestParam("contact-method") String contactMethod,
            Model model) {

        // Create and save the ContactMessage
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setFirstName(firstName);
        contactMessage.setLastName(lastName);
        contactMessage.setEmail(email);
        contactMessage.setPhoneNumber(phoneNumber);
        contactMessage.setMessage(message);
        contactMessage.setInquiryType(ContactMessage.InquiryType.valueOf(inquiryType.toUpperCase()));
        contactMessage.setContactMethod(ContactMessage.ContactMethod.valueOf(contactMethod.toUpperCase()));

        // Save the contact message to the database
        contactMessageService.saveMessage(contactMessage);

        // Add attributes to model if needed
        model.addAttribute("contactMessage", contactMessage);

        // Return success page
        return "/beforeLoginPages/contact-us-success";
    }

    @GetMapping("/messages")
    public String showMessages(Model model) {
        List<ContactMessage> visibleMessages = contactMessageService.getVisibleMessages();
        model.addAttribute("messages", visibleMessages);

        model.addAttribute("activePage", "/contact/messages");
        model.addAttribute("activeDropdown", "contact");
        return "contact-message/contactMessages";
    }

    // Mark a message as resolved
    @PostMapping("/messages/{id}/resolve")
    public String resolveMessage(@PathVariable Long id) {
        contactMessageService.resolveMessage(id);
        return "redirect:/contact/messages"; // Redirect back to the messages dashboard
    }

    // View message details and reply to it
    @GetMapping("/messages/{id}")
    public String viewMessage(@PathVariable Long id, Model model) {
        ContactMessage message = contactMessageService.getMessageById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid message Id:" + id));
        model.addAttribute("message", message);
        return "/contact-message/viewMessage"; // View name for viewing and replying to the message
    }

    @PostMapping("/messages/{id}/removeFromDashboard")
    public ResponseEntity<String> removeFromDashboard(@PathVariable Long id) {
        // Mark the message as "hidden" without deleting it from the database
        contactMessageService.hideMessageFromDashboard(id);
        return ResponseEntity.ok("Message removed from the dashboard.");
    }

    @PostMapping("/messages/{id}/delete")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        contactMessageService.deleteMessage(id);
        return ResponseEntity.ok("Message deleted from the database and dashboard.");
    }
}
