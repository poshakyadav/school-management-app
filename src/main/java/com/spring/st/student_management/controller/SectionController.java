package com.spring.st.student_management.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.st.student_management.model.Section;
import com.spring.st.student_management.service.SectionService;

@Controller
@RequestMapping("/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    // List all sections
    @GetMapping("/list")
    public String listSections(Model model) {
        model.addAttribute("sections", sectionService.getAllSections());
        model.addAttribute("activePage", "/sections/list");
        model.addAttribute("activeDropdown", "classAndSection");
        return "sections/list";
    }

    // Show form to add a new section
    @GetMapping("/add")
    public String addSectionForm(Model model) {
        model.addAttribute("section", new Section());
        model.addAttribute("activePage", "/sections/add");
        model.addAttribute("activeDropdown", "classAndSection");
        return "sections/form";
    }

    // Save the new section
    @PostMapping("/add")
    public String addSection(@ModelAttribute Section section) {
        sectionService.addSection(section);
        return "redirect:/sections/list";
    }

    // Show form to edit an existing section
    @GetMapping("/edit/{id}")
    public String editSectionForm(@PathVariable("id") Long id, Model model) {
        Section section = sectionService.getSectionById(id);
        model.addAttribute("section", section);
        return "sections/form";
    }

    // Update the section
    @PostMapping("/edit")
    public String updateSection(@ModelAttribute Section section) {
        sectionService.updateSection(section.getId(), section);
        return "redirect:/sections/list";
    }

    // Delete a section
    @GetMapping("/delete/{id}")
    public String deleteSection(@PathVariable("id") Long id) {
        sectionService.deleteSection(id);
        return "redirect:/sections/list";
    }
}

