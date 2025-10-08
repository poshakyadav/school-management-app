package com.spring.st.student_management.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.AcademicYear;
import com.spring.st.student_management.model.FeeAllocation;
import com.spring.st.student_management.model.FeeStructure;
import com.spring.st.student_management.model.Student;
import com.spring.st.student_management.model.StudentClass;
import com.spring.st.student_management.model.StudentHistory;
import com.spring.st.student_management.repository.AcademicYearRepository;
import com.spring.st.student_management.repository.FeeAllocationRepository;
import com.spring.st.student_management.repository.StudentClassRepository;
import com.spring.st.student_management.repository.StudentHistoryRepository;
import com.spring.st.student_management.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class StudentPromotionService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentHistoryRepository studentHistoryRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @Autowired
    private FeeAllocationRepository feeAllocationRepository;


    @Transactional
    public void promoteStudents(List<Long> studentIds, Long nextClassId, Long nextAcademicYearId) {
        // Fetch the next class and academic year
        StudentClass nextClass = studentClassRepository.findById(nextClassId)
                .orElseThrow(() -> new RuntimeException("Next class not found"));

        AcademicYear nextAcademicYear = academicYearRepository.findById(nextAcademicYearId)
                .orElseThrow(() -> new RuntimeException("Next academic year not found"));

        // Fetch the students to promote
        List<Student> studentsToPromote = studentRepository.findAllById(studentIds);

        // Save current details to student history
        List<StudentHistory> studentHistories = studentsToPromote.stream().map(student -> {
            StudentHistory history = new StudentHistory();
            history.setStudent(student);
            history.setPreviousClass(student.getStudentClass());
            history.setPreviousAcademicYear(student.getAcademicYear());
            history.setGrades("N/A"); // Replace with actual grades
            history.setTotalMarks(0); // Replace with actual marks
            history.setTotalFeePaid(student.getTotalPayments());
            history.setRemarks("Promoted to next class");
            return history;
        }).collect(Collectors.toList());
        studentHistoryRepository.saveAll(studentHistories);

        // Update students' current class and academic year
        studentsToPromote.forEach(student -> {
            student.setStudentClass(nextClass);
            student.setAcademicYear(nextAcademicYear);

            System.out.println("Promoting student: " + student.getId());
            System.out.println("Next Class: " + nextClass);

            List<FeeStructure> nextClassFeeStructures = nextClass.getFeeStructures();
            System.out.println("Next class Fee Structures: " + nextClassFeeStructures);

            if (nextClassFeeStructures.isEmpty()) {
                student.setOutstandingBalance(0.0);
                System.out.println("No fee structures found for next class: " + nextClass.getId());
                student.getFeeAllocations().clear();
                student.getFeeStructures().clear();
            } else {
                // Calculate total fee amount for all fee structures
                double totalFeeAmount = nextClassFeeStructures.stream()
                        .mapToDouble(FeeStructure::getAmount)
                        .sum();

                // Total payments made by the student
                double totalPayments = student.getTotalPayments() != null ? student.getTotalPayments() : 0.0;

                // Set the final outstanding balance
                double updatedOutstanding = totalFeeAmount - totalPayments;
                student.setOutstandingBalance(updatedOutstanding);

                // Allocate fees for each structure
                nextClassFeeStructures.forEach(feeStructure -> {
                    FeeAllocation allocation = new FeeAllocation();
                    allocation.setStudent(student);
                    allocation.setFeeStructure(feeStructure);
                    allocation.setTotalAmount(feeStructure.getAmount());
                    allocation.setDiscount(0.0); // Adjust if discounts are applicable
                    allocation.setNetAmount(feeStructure.getAmount());
                    feeAllocationRepository.save(allocation);
                    System.out.println("Allocated fee for student: " + student.getId() + " | Fee Type: "
                            + feeStructure.getFeeType() + " | Amount: " + feeStructure.getAmount());
                });

                System.out.println(
                        "Final Outstanding Balance for student " + student.getId() + ": " + updatedOutstanding);
            }
        });

        // Save updated students
        studentRepository.saveAll(studentsToPromote);
    }

}
