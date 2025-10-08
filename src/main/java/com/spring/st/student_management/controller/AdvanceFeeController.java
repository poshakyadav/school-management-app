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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.st.student_management.model.FeeAllocation;
import com.spring.st.student_management.model.FeeStructure;
import com.spring.st.student_management.model.Payment;
import com.spring.st.student_management.model.Student;
import com.spring.st.student_management.model.StudentClass;
import com.spring.st.student_management.repository.StudentRepository;
import com.spring.st.student_management.service.AcademicYearService;
import com.spring.st.student_management.service.AdvancedFeeService;
import com.spring.st.student_management.service.FeeStructureService;
import com.spring.st.student_management.service.StudentClassService;

@Controller
@RequestMapping("/adv-fees")
public class AdvanceFeeController {

    @Autowired
    private AdvancedFeeService feeService;

    @Autowired
    private FeeStructureService feeStructureService;

    @Autowired
    private StudentClassService studentClassService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AcademicYearService academicYearService;

    // List allocated fees for a student
    @GetMapping("/list/{studentId}")
    public String listFees(@PathVariable("studentId") Long studentId, Model model) {
        List<FeeAllocation> fees = feeService.getAllocatedFeesByStudentId(studentId);
        model.addAttribute("fees", fees);
        model.addAttribute("studentId", studentId);
        return "adv-fees/list";
    }

    // Show fee allocation form
    @GetMapping("/allocate/{studentId}")
    public String showAllocateForm(@PathVariable("studentId") Long studentId, Model model) {
        model.addAttribute("feeStructures", feeStructureService.getAllFeeStructures());
        model.addAttribute("studentId", studentId);
        return "adv-fees/form";
    }

    // Allocate fee to a student
    @PostMapping("/allocate")
    public String allocateFee(@RequestParam Long studentId,
            @RequestParam Long feeStructureId,
            @RequestParam Double discount) {
        feeService.allocateFee(studentId, feeStructureId, discount);
        return "redirect:/adv-fees/list/" + studentId;
    }

    // -----------------------------------------------------------------------------------

    // Show payment form
    @GetMapping("/pay/{studentId}")
    public String showPaymentForm(@PathVariable("studentId") Long studentId, Model model) {
        model.addAttribute("studentId", studentId);
        return "adv-fees/pay";
    }

    @PostMapping("/pay")
    public String makePayment(@RequestParam Long studentId, @RequestParam Double amount,
            RedirectAttributes redirectAttributes) {
        feeService.makePayment(studentId, amount);
        redirectAttributes.addFlashAttribute("message", "Payment of " + amount + " made successfully.");
        return "redirect:/students/list";
    }

    // Show outstanding balance
    @GetMapping("/outstanding/{studentId}")
    public String showOutstanding(@PathVariable("studentId") Long studentId, Model model) {
        // Double outstanding = feeService.calculateOutstanding(studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        Double outstanding = student.getOutstandingBalance();

        model.addAttribute("outstanding", outstanding);
        return "adv-fees/outstanding";
    }

    // View payment history
    @GetMapping("/payments/{studentId}")
    public String viewPaymentHistory(@PathVariable("studentId") Long studentId, Model model) {
        List<Payment> payments = feeService.getPaymentHistoryByStudentId(studentId);
        model.addAttribute("payments", payments);
        return "adv-fees/payments";
    }

    // Show form to add a fee structure
    @GetMapping("/add-fee-structure")
    public String showAddFeeStructureForm(Model model) {
        model.addAttribute("feeStructure", new FeeStructure());
        model.addAttribute("academicYears", academicYearService.getAllAcademicYears()); // Fetch academic years

        model.addAttribute("activePage", "/adv-fees/add-fee-structure");
        model.addAttribute("activeDropdown", "feeStructure");
        return "adv-fees/add-fee-structure";
    }

    @PostMapping("/add-fee-structure")
    public String addFeeStructure(@ModelAttribute FeeStructure feeStructure) {
        // Set any additional attributes not coming from the form, if necessary
        feeStructure.setStatus("Active");

        // Save the fee structure
        feeStructureService.addFeeStructure(feeStructure);

        return "redirect:/adv-fees/list-fee-structures";
    }

    // List all fee structures
    @GetMapping("/list-fee-structures")
    public String listFeeStructures(Model model) {
        List<FeeStructure> feeStructures = feeStructureService.getAllFeeStructures();
        model.addAttribute("feeStructures", feeStructures);
        model.addAttribute("activePage", "/adv-fees/list-fee-structures");
        model.addAttribute("activeDropdown", "feeStructure");
        return "adv-fees/list-fee-structures";
    }

    // For assign fee structure to class 
    @GetMapping("/assign-fee-structure")
    public String showAssignFeeStructureForm(Model model) {
        model.addAttribute("classes", studentClassService.findAll());
        model.addAttribute("feeStructures", feeStructureService.getAllFeeStructures());
        model.addAttribute("activePage", "/adv-fees/assign-fee-structure");
        model.addAttribute("activeDropdown", "feeStructure");
        return "adv-fees/assign-fee-structure";
    }

    @PostMapping("/assign-fee-structure")
    public String assignFeeStructureToClass(@RequestParam Long classId,
            @RequestParam Long feeStructureId) {
        feeStructureService.allocateFeeStructureToClass(classId, feeStructureId);
        return "redirect:/adv-fees/list-fee-structures";
    }

    // Add API to fetch academic year for a class
    @GetMapping("/class-academic-year/{classId}")
    @ResponseBody
    public String getClassAcademicYear(@PathVariable Long classId) {
        System.out.println("Records fetching start ..................... in getClassAcademicYear on controller -----------");
        StudentClass studentClass = studentClassService.getClassById(classId);
        
        return studentClass.getAcademicYear().getName(); // Assuming `getAcademicYear()` returns an entity with
                                                         // `getName()`
    }

    // Add API to fetch academic year for a fee structure
    @GetMapping("/fee-structure-academic-year/{feeStructureId}")
    @ResponseBody
    public String getFeeStructureAcademicYear(@PathVariable Long feeStructureId) {
        System.out.println("Records fetching start ..................... in getFeeStructureAcademicYear on controller -----------");
        FeeStructure feeStructure = feeStructureService.getFeeStructureById(feeStructureId);
        return feeStructure.getAcademicYear().getName(); // Assuming `getAcademicYear()` returns an entity with
                                                         // `getName()`
    }

    // Edit fee structure page
    @GetMapping("/edit-fee-structure/{id}")
    public String editFeeStructure(@PathVariable("id") Long id, Model model) {
        FeeStructure feeStructure = feeStructureService.getFeeStructureById(id);
        model.addAttribute("feeStructure", feeStructure);
        model.addAttribute("academicYears", academicYearService.getAllAcademicYears()); // Fetch academic years
        return "adv-fees/edit-fee-structure";
    }

    @PostMapping("/edit-fee-structure")
    public String updateFeeStructure(@ModelAttribute FeeStructure feeStructure) {
        // Fetch the existing FeeStructure entity to ensure it exists
        FeeStructure existingFeeStructure = feeStructureService.getFeeStructureById(feeStructure.getId());

        // Update the fields using the bound `@ModelAttribute` data
        // existingFeeStructure.setClassId(feeStructure.getClassId());
        existingFeeStructure.setFeeType(feeStructure.getFeeType());
        existingFeeStructure.setAcademicYear(feeStructure.getAcademicYear());
        existingFeeStructure.setAmount(feeStructure.getAmount());
        existingFeeStructure.setDescription(feeStructure.getDescription());
        existingFeeStructure.setDueDate(feeStructure.getDueDate());

        // Save the updated FeeStructure entity
        feeStructureService.updateFeeStructure(existingFeeStructure);

        return "redirect:/adv-fees/list-fee-structures";
    }

    // Delete fee structure
    @GetMapping("/delete-fee-structure/{id}")
    public String deleteFeeStructure(@PathVariable("id") Long id) {
        feeStructureService.deleteFeeStructure(id);
        return "redirect:/adv-fees/list-fee-structures";
    }

    // Remove a class from a fee structure
    @GetMapping("/remove-class-from-structure")
    public String removeClassFromFeeStructure(@RequestParam Long classId, @RequestParam Long feeStructureId) {
        feeStructureService.removeClassFromFeeStructure(classId, feeStructureId);
        return "redirect:/adv-fees/list-fee-structures";
    }

}
