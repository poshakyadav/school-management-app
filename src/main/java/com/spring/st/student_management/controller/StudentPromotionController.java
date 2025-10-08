package com.spring.st.student_management.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.st.student_management.model.Student;
import com.spring.st.student_management.repository.StudentRepository;
import com.spring.st.student_management.service.StudentPromotionService;

@Controller
@RequestMapping("/api/student-promotion")
public class StudentPromotionController {

    @Autowired
    private StudentPromotionService studentPromotionService;

    @Autowired
    private StudentRepository studentRepository;

    // @PostMapping("/promote")
    // public ResponseEntity<String> promoteStudents(@RequestBody
    // PromoteStudentsRequest request) {
    // studentPromotionService.promoteStudents(request.getStudentIds(),
    // request.getNextClassId(), request.getNextAcademicYearId());
    // return ResponseEntity.ok("Students promoted successfully!");
    // }

    @PostMapping("/promote")
    public String promoteStudents(
            @RequestParam("studentIds") List<Long> studentIds,
            @RequestParam("nextClassId") Long nextClassId,
            @RequestParam("nextAcademicYearId") Long nextAcademicYearId,
            Model model) {

        if (studentIds == null || studentIds.isEmpty()) {
            model.addAttribute("error", "No students selected for promotion.");
            return "promotion/promotion";
        }

        // Perform promotion logic in the service
        studentPromotionService.promoteStudents(studentIds, nextClassId, nextAcademicYearId);

        // Add any success messages if required
        model.addAttribute("message", "Students have been successfully promoted.");

        // Redirect to the success page
        return "promotion/promotion_success";
    }

    @GetMapping("/fetch-students")
    @ResponseBody
    public List<Map<String, Object>> fetchStudents(
            @RequestParam("classId") Long classId,
            @RequestParam("academicYearId") Long academicYearId) {
        List<Student> students = studentRepository.findByStudentClassIdAndAcademicYearId(classId, academicYearId);
        return students.stream().map(student -> {
            Map<String, Object> studentMap = new HashMap<>();
            studentMap.put("id", student.getId());
            studentMap.put("firstName", student.getFirstName());
            studentMap.put("lastName", student.getLastName());
            studentMap.put("className", student.getStudentClass().getName());
            studentMap.put("sectionName", student.getSection().getName());
            studentMap.put("academicYear",
                    student.getAcademicYear().getStartDate() + " - " + student.getAcademicYear().getEndDate());
            return studentMap;
        }).collect(Collectors.toList());
    }

}