package com.spring.st.student_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.Section;
import com.spring.st.student_management.repository.SectionRepository;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    public Section addSection(Section section) {
        return sectionRepository.save(section);
    }

    public Section updateSection(Long id, Section section) {
        Section existingSection = sectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        existingSection.setName(section.getName());
        return sectionRepository.save(existingSection);
    }

    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }

    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }
    public List<Section> findSectionsByIds(List<Long> ids) {
        return sectionRepository.findAllById(ids);
    }

    public Section getSectionById(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));
    }

    // Find a course by its ID
    public Section findById(Long id) {
        return sectionRepository.findById(id).orElse(null);
    }

    public List<Section> findAllById(List<Long> ids) {
        return sectionRepository.findAllById(ids);
    }

    // Retrieve all sections
    public List<Section> findAll() {
        return sectionRepository.findAll();
    }
}

