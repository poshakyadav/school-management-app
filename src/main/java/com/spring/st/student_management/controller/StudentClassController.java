package com.spring.st.student_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.st.student_management.model.AcademicYear;
import com.spring.st.student_management.model.Section;
import com.spring.st.student_management.model.StudentClass;
import com.spring.st.student_management.service.AcademicYearService;
import com.spring.st.student_management.service.SectionService;
import com.spring.st.student_management.service.StudentClassService;

@Controller
@RequestMapping("/classes")
public class StudentClassController {

    @Autowired
    private StudentClassService studentClassService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private AcademicYearService academicYearService;

    @GetMapping("/list")
    public String listClasses(Model model) {
        List<StudentClass> classes = studentClassService.getAllClassesWithSections();
        model.addAttribute("classes", classes);
        model.addAttribute("classes", studentClassService.getAllClasses());
        model.addAttribute("activePage", "/classes/list");
        model.addAttribute("activeDropdown", "classAndSection");
        return "classes/list";
    }

    @GetMapping("/add")
    public String addClassForm(Model model) {
        model.addAttribute("studentClass", new StudentClass());
        List<Section> allSections = sectionService.getAllSections();
        System.out.println("AllSections : " + allSections); 
        model.addAttribute("sections", allSections);

        List<AcademicYear> academicYears = academicYearService.getAllAcademicYears();
        System.out.println("AcademicYears: " + academicYears); // Debug log
        model.addAttribute("academicYears", academicYears);
        model.addAttribute("activePage", "/classes/add");
        model.addAttribute("activeDropdown", "classAndSection");
        return "classes/form";
    }

    @PostMapping("/add")
    public String addClass(@ModelAttribute StudentClass studentClass,
            @RequestParam(name = "sections", required = false) List<Long> sectionIds) {
        if (sectionIds != null) {
            // Load sections and associate them with the class
            List<Section> sections = sectionService.findSectionsByIds(sectionIds);
            studentClass.setSections(sections);
            // Update the bidirectional relationship
            for (Section section : sections) {
                section.getStudentClasses().add(studentClass);
            }
        }
        studentClassService.saveClass(studentClass);
        return "redirect:/classes/list";
    }

    @GetMapping("/edit/{id}")
    public String editClassForm(@PathVariable("id") Long id, Model model) {
        StudentClass studentClass = studentClassService.findById(id);
        model.addAttribute("studentClass", studentClassService.getClassById(id));
        model.addAttribute("sections", sectionService.getAllSections());
        model.addAttribute("studentClass", studentClass);

        model.addAttribute("academicYears", academicYearService.getAllAcademicYears());

        return "classes/form";
    }

    @PostMapping("/edit")
    public String updateClass(@ModelAttribute StudentClass studentClass,
            @RequestParam(name = "sections", required = false) List<Long> sectionIds) {
        if (sectionIds != null) {
            // Load sections and associate them with the updated class
            List<Section> sections = sectionService.findSectionsByIds(sectionIds);
            studentClass.setSections(sections);
            // Update the bidirectional relationship
            for (Section section : sections) {
                if (!section.getStudentClasses().contains(studentClass)) {
                    section.getStudentClasses().add(studentClass);
                }
            }
        } else {
            studentClass.setSections(null); // Clear sections if none are selected
        }
        studentClassService.saveClass(studentClass);
        return "redirect:/classes/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteClass(@PathVariable("id") Long id) {
        studentClassService.deleteClass(id);
        return "redirect:/classes/list";
    }
}
