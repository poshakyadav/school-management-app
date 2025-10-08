package com.spring.st.student_management.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.AcademicYear;
import com.spring.st.student_management.model.FeeAllocation;
import com.spring.st.student_management.model.FeeStructure;
import com.spring.st.student_management.model.Payment;
import com.spring.st.student_management.model.Student;
import com.spring.st.student_management.model.StudentClass;
import com.spring.st.student_management.repository.FeeAllocationRepository;
import com.spring.st.student_management.repository.FeeStructureRepository;
import com.spring.st.student_management.repository.StudentClassRepository;
import com.spring.st.student_management.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class FeeStructureService {

    @Autowired
    private FeeStructureRepository feeStructureRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FeeAllocationRepository feeAllocationRepository;

    // Retrieve all fee structures
    public List<FeeStructure> getAllFeeStructures() {
        return feeStructureRepository.findAll();
    }

    // Retrieve fee structure by ID
    public FeeStructure getFeeStructureById(Long id) {
        Optional<FeeStructure> feeStructure = feeStructureRepository.findById(id);
        return feeStructure.orElse(null);
    }

    // Add or update a fee structure
    public FeeStructure addFeeStructure(FeeStructure feeStructure) {
        return feeStructureRepository.save(feeStructure);
    }

    // Retrieve fee structures for a specific class and academic year
    public List<FeeStructure> getFeeStructuresByClassAndYear(Long classId, AcademicYear academicYear) {
        return feeStructureRepository.findByClassIdAndAcademicYear(classId, academicYear);
    }

    public void allocateFeeStructureToClass(Long classId, Long feeStructureId) {

        StudentClass studentClass = studentClassRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        FeeStructure feeStructure = feeStructureRepository.findById(feeStructureId)
                .orElseThrow(() -> new RuntimeException("Fee structure not found"));

        // Add fee structure to the class
        studentClass.getFeeStructures().add(feeStructure);
        studentClassRepository.save(studentClass);

        // Allocate fee to all students in the class
        List<Student> students = studentRepository.findByStudentClass(studentClass);
        for (Student student : students) {
            // Check if a FeeAllocation already exists for this student and fee structure
            Optional<FeeAllocation> existingAllocation = feeAllocationRepository
                    .findByStudentIdAndFeeStructureId(student.getId(), feeStructureId);
            if (existingAllocation.isEmpty()) {
                // Create a new FeeAllocation
                FeeAllocation feeAllocation = new FeeAllocation();
                feeAllocation.setStudent(student);
                feeAllocation.setFeeStructure(feeStructure);
                feeAllocation.setTotalAmount(feeStructure.getAmount());
                feeAllocation.setDiscount(0.0); // Or set based on input
                feeAllocation.setNetAmount(feeStructure.getAmount()); // Update this based on discount
                feeAllocationRepository.save(feeAllocation);

                // Handle outstanding balance as null (if necessary)
                double outstandingBalance = student.getOutstandingBalance() != null ? student.getOutstandingBalance()
                        : 0.0;

                double previouslyPaid = student.getTotalPayments() != null ? student.getTotalPayments() : 0.0;

                if (previouslyPaid > 0 && outstandingBalance == 0) {
                    // Adjust the outstanding balance by deducting the paid amount
                    double adjustedOutstandingBalance = outstandingBalance + feeStructure.getAmount() - previouslyPaid;
                    student.setOutstandingBalance(adjustedOutstandingBalance);
                } else {
                    // Set the full fee structure amount as the outstanding balance
                    double newOutstandingBalance = outstandingBalance + feeStructure.getAmount();
                    student.setOutstandingBalance(newOutstandingBalance);
                }

                // Save the updated student record
                studentRepository.save(student);

            }
        }
    }

    public void updateFeeStructure(FeeStructure feeStructure) {
        // Fetch the original FeeStructure to get the old amount
        FeeStructure originalFeeStructure = feeStructureRepository.findById(feeStructure.getId())
                .orElseThrow(() -> new RuntimeException("Fee structure not found"));

        // double oldAmount = originalFeeStructure.getAmount();
        double newAmount = feeStructure.getAmount();

        // Update and save the fee structure
        originalFeeStructure.setFeeType(feeStructure.getFeeType());
        originalFeeStructure.setAcademicYear(feeStructure.getAcademicYear());
        originalFeeStructure.setAmount(newAmount);
        originalFeeStructure.setDescription(feeStructure.getDescription());
        originalFeeStructure.setDueDate(feeStructure.getDueDate());
        feeStructureRepository.save(originalFeeStructure);

        // Update all associated FeeAllocations
        List<FeeAllocation> feeAllocations = feeAllocationRepository.findByFeeStructureId(feeStructure.getId());
        for (FeeAllocation allocation : feeAllocations) {
            allocation.setTotalAmount(newAmount); // Update the total amount
            allocation.setNetAmount(newAmount - allocation.getDiscount()); // Recalculate the net amount
            feeAllocationRepository.save(allocation);
        }

        // Recalculate and update outstanding balances for all affected students
        Set<Student> affectedStudents = feeAllocations.stream()
                .map(FeeAllocation::getStudent)
                .collect(Collectors.toSet());

        for (Student student : affectedStudents) {
            double totalOutstanding = calculateTotalOutstandingForStudent(student);
            student.setOutstandingBalance(totalOutstanding);
            studentRepository.save(student);
        }
    }

    // Helper method to calculate the total outstanding balance for a student
    private double calculateTotalOutstandingForStudent(Student student) {
        List<FeeAllocation> studentFeeAllocations = feeAllocationRepository.findByStudentId(student.getId());
        double totalFeeAmount = studentFeeAllocations.stream()
                .mapToDouble(FeeAllocation::getNetAmount)
                .sum();

        double totalPayments = student.getPayments().stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        return totalFeeAmount - totalPayments;
    }

    // Delete a fee structure by ID
    public void deleteFeeStructure(Long id) {
        feeStructureRepository.deleteById(id);
    }

    // To show assigned classes (this depends on your data model, assuming a mapping
    // exists)
    public List<String> getAssignedClassesForFeeStructure(Long feeStructureId) {
        // Retrieve the list of assigned classes for the given fee structure
        // This assumes that FeeStructure has a relationship with Class entities
        return feeStructureRepository.findAssignedClasses(feeStructureId);
    }

    // Fetch all fee structures with their associated classes
    public List<FeeStructure> getAllFeeStructuresWithClasses() {
        return feeStructureRepository.findAllWithClasses();
    }

    @Transactional
    public void removeClassFromFeeStructure(Long classId, Long feeStructureId) {
        // Fetch the class and fee structure
        StudentClass studentClass = studentClassRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        FeeStructure feeStructure = feeStructureRepository.findById(feeStructureId)
                .orElseThrow(() -> new RuntimeException("Fee structure not found"));

        // Remove the fee structure from the class
        studentClass.getFeeStructures().remove(feeStructure);
        studentClassRepository.save(studentClass);

        // Process fee allocations for students
        // Remove associated FeeAllocations for students
        List<Student> students = studentRepository.findByStudentClass(studentClass);
        for (Student student : students) {
            // Get the allocation for this fee structure
            Optional<FeeAllocation> feeAllocation = feeAllocationRepository
                    .findByStudentIdAndFeeStructureId(student.getId(), feeStructureId);
            feeAllocation.ifPresent(allocation -> {

                // Handle outstanding balance as null (if necessary)
                double outstandingBalance = student.getOutstandingBalance() != null ? student.getOutstandingBalance()
                        : 0.0;

                // Adjust the outstanding balance by removing the fee amount
                double newOutstandingBalance = outstandingBalance - allocation.getTotalAmount();
                // student.setOutstandingBalance(newOutstandingBalance);

                // studentRepository.save(student); // Save the updated student

                // Delete the fee allocation
                feeAllocationRepository.delete(allocation);

                // Check if the student has any remaining FeeAllocations
                List<FeeAllocation> remainingAllocations = feeAllocationRepository.findByStudentId(student.getId());
                if (remainingAllocations.isEmpty()) {
                    // No remaining allocations, set outstanding balance to 0
                    student.setOutstandingBalance(0.0);
                } else {
                    // Set the new calculated outstanding balance
                    student.setOutstandingBalance(newOutstandingBalance);
                }

                // Save the updated student record
                studentRepository.save(student);
            });

        }

        // Additional cleanup if necessary, like removing related allocations
        feeAllocationRepository.deleteByFeeStructureAndClassId(feeStructureId, classId);

    }

    public List<FeeStructure> getFeeStructuresByClassAndYear(Long classId, Long academicYearId) {
        return feeStructureRepository.findByClassIdAndAcademicYearId(classId, academicYearId);
    }

}