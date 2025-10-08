package com.spring.st.student_management.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.st.student_management.model.FeeStructure;
import com.spring.st.student_management.model.Student;
import com.spring.st.student_management.service.AcademicYearService;
import com.spring.st.student_management.service.AdvancedFeeService;
import com.spring.st.student_management.service.SectionService;
import com.spring.st.student_management.service.StudentClassService;
import com.spring.st.student_management.service.StudentService;
import com.spring.st.student_management.service.SubjectService;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentClassService studentClassService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private AdvancedFeeService advancedFeeService;

    @Autowired
    private AcademicYearService academicYearService;

    @GetMapping("/list")
    public String listStudents(Model model) {
        List<Student> students = studentService.findAll();
        Map<Long, Double> assignedFees = new HashMap<>();
        Map<Long, Double> payments = new HashMap<>();
        Map<Long, Double> outstandingBalances = new HashMap<>();

        for (Student student : students) {
            Long studentId = student.getId();
            assignedFees.put(studentId, advancedFeeService.getTotalAllocatedFee(studentId));
            payments.put(studentId, advancedFeeService.getTotalPayments(studentId));
            outstandingBalances.put(studentId, advancedFeeService.calculateOutstanding(studentId));

            student.setFeeStructures(advancedFeeService.getFeeStructuresByStudent(studentId));
        }

        model.addAttribute("students", students);
        model.addAttribute("assignedFees", assignedFees);
        model.addAttribute("payments", payments);
        model.addAttribute("outstandingBalances", outstandingBalances);
        model.addAttribute("activePage", "/students/list");
        model.addAttribute("activeDropdown", "students");
        return "students/list";
    }

    @GetMapping("/add")
    public String addStudentForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("studentClasses", studentClassService.findAll());
        model.addAttribute("subject", subjectService.findAll()); // Pass courses for the dropdown
        model.addAttribute("sections", sectionService.findAll());
        model.addAttribute("academicYears", academicYearService.getAllAcademicYears()); // Fetch academic years
        model.addAttribute("activePage", "/students/add");
        model.addAttribute("activeDropdown", "students");

        return "students/form";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute Student student) {
        // Save the student first to generate an ID
        student = studentService.save(student);

        // Fetch the class and section of the student
        Long studentClassId = student.getStudentClass().getId(); // Assuming `studentClass` is not null
        Long sectionId = student.getSection().getId(); // Assuming `section` is not null

        // Fetch the fee structure assigned to the class and section
        List<FeeStructure> feeStructures = advancedFeeService.getFeeStructuresForClassAndSection(studentClassId,
                sectionId);

        // Assign the fee structure to the new student
        if (!feeStructures.isEmpty()) {
            for (FeeStructure feeStructure : feeStructures) {
                advancedFeeService.allocateFee(student.getId(), feeStructure.getId(), 0.0); // No discount for now
            }
        }

        return "redirect:/students/list";
    }

    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        model.addAttribute("studentClasses", studentClassService.findAll());
        model.addAttribute("sections", sectionService.findAll());
        model.addAttribute("academicYears", academicYearService.getAllAcademicYears()); // Fetch academic years
        return "students/student-edit-form";
    }

    @PostMapping("/edit")
    public String updateStudent(@ModelAttribute Student student) {
        studentService.updateStudent(student);
        return "redirect:/students/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.delete(id);
        return "redirect:/students/list";
    }

    @GetMapping("/filter-page")
    public String filterPage(@RequestParam(required = false) Long classId,
            @RequestParam(required = false) Long academicYearId,
            Model model) {
        model.addAttribute("studentClasses", studentClassService.findAll());
        model.addAttribute("academicYears", academicYearService.getAllAcademicYears());
        model.addAttribute("selectedClassId", classId);
        model.addAttribute("selectedAcademicYearId", academicYearId);
        model.addAttribute("activePage", "/students/filter-page");
        model.addAttribute("activeDropdown", "students");

        return "students/filter";
    }

    @GetMapping("/filter")
    @ResponseBody
    public ResponseEntity<?> filterStudents(
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Long academicYearId) {

        if (classId == null || academicYearId == null) {
            return ResponseEntity.badRequest().body("Class ID and Academic Year ID are required.");
        }

        List<Student> students = studentService.findStudentsByClassAndAcademicYear(classId, academicYearId);

        if (students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No students found for the selected class and academic year.");
        }

        Map<Long, Double> assignedFees = new HashMap<>();
        Map<Long, Double> totalPayments = new HashMap<>();
        Map<Long, Double> outstandingBalances = new HashMap<>();

        for (Student student : students) {
            Long studentId = student.getId();
            assignedFees.put(studentId, advancedFeeService.getTotalAllocatedFee(studentId));
            totalPayments.put(studentId, advancedFeeService.getTotalPayments(studentId));
            outstandingBalances.put(studentId, advancedFeeService.calculateOutstanding(studentId));
        }

        List<Map<String, Object>> studentData = students.stream().map(student -> {
            Map<String, Object> studentMap = new HashMap<>();
            studentMap.put("id", student.getId());
            studentMap.put("firstName", student.getFirstName());
            studentMap.put("lastName", student.getLastName());
            studentMap.put("dateOfBirth", student.getDateOfBirth());
            studentMap.put("email", student.getEmail());
            studentMap.put("studentClass", student.getStudentClass().getName());
            studentMap.put("section", student.getSection() != null ? student.getSection().getName() : "N/A");
            studentMap.put("academicYear", Map.of(
                    "startDate", student.getAcademicYear().getStartDate(),
                    "endDate", student.getAcademicYear().getEndDate()));
            studentMap.put("subject", student.getSubject() != null ? student.getSubject().getName() : "N/A");
            studentMap.put("assignedFees", assignedFees.get(student.getId()));
            studentMap.put("totalPayments", student.getTotalPayments());
            studentMap.put("outstandingBalance", student.getOutstandingBalance());
            return studentMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(studentData);
    }

}
