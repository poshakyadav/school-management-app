package com.spring.st.student_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.FeeStructure;
import com.spring.st.student_management.model.Student;
import com.spring.st.student_management.model.StudentClass;
import com.spring.st.student_management.repository.FeeAllocationRepository;
import com.spring.st.student_management.repository.PaymentRepository;
import com.spring.st.student_management.repository.StudentClassRepository;
import com.spring.st.student_management.repository.StudentRepository;
import com.spring.st.student_management.util.RoleConstants;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FeeAllocationRepository feeAllocationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student save(Student student) {

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setRoles(List.of(RoleConstants.ROLE_STUDENT));

        return studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll(); // Retrieve all students
    }

    public void delete(Long id) {
        studentRepository.deleteById(id); // Delete a student by ID
    }

    public List<Student> findStudentsByClass(Long classId) {
        StudentClass studentClass = studentClassRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        return studentRepository.findByStudentClass(studentClass);
    }

    public void updateStudentFeeStructures(Long studentId, List<FeeStructure> feeStructures) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        student.setFeeStructures(feeStructures);
        studentRepository.save(student);
    }

    public String getPasswordByStudentId(Long studentId) {
        return studentRepository.findById(studentId)
                .map(Student::getPassword)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    public Double getTotalPaymentByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        // Log the actual value being fetched
        System.out.println(
                "Total Payments for Student ID " + studentId + ": " + student.getTotalPayments() + "/////////////////");

        return student.getTotalPayments() != null ? student.getTotalPayments() : 0.0;
    }

    public Double getTotalAllocatedFee(Long studentId) {
        return feeAllocationRepository.getTotalAllocatedFeeByStudentId(studentId);
    }

    public void updateTotalPayment(Long studentId, Double totalPayments) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        // Step 2: Set the total payments
        student.setTotalPayments(totalPayments);

        // Step 1: Only update the outstanding balance once, this will update the
        // student with the correct balance.
        updateOutstandingBalance(studentId);

        // Step 3: Save the student entity
        studentRepository.save(student);
    }

    public Double getOutStandingbalance(Long studentId) {
        Double outstandingBalanceById = studentRepository.findOutstandingBalanceByStudentId(studentId);
        return outstandingBalanceById;
    }

    public void updateOutstandingBalance(Long studentId) {
        // Step 1: Get total allocated fees for the student
        Double allocatedFees = getTotalAllocatedFee(studentId); // Get total fee allocation from FeeAllocation table

        // Step 2: Get total payments made by the student
        Double totalPayments = paymentRepository.getTotalPaymentsByStudentId(studentId);

        // Step 3: Calculate the outstanding balance (allocatedFees - totalPayments)
        Double outstandingBalance = allocatedFees - totalPayments;

        // Step 4: Retrieve the student entity and update the outstanding balance
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        student.setOutstandingBalance(outstandingBalance);

        // Step 5: Save the updated student entity
        studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(Student student) {
        // Fetch the existing student from the database
        Student existingStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + student.getId()));

        // Update fields except the relationships
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setPhoneNum(student.getPhoneNum());
        existingStudent.setDateOfBirth(student.getDateOfBirth());
        existingStudent.setStudentClass(student.getStudentClass());
        existingStudent.setSection(student.getSection());

        // Save the existing student without modifying payments or fee allocations
        return studentRepository.save(existingStudent);
    }

    public List<Student> findStudentsByClassAndAcademicYear(Long classId, Long academicYearId) {
        return studentRepository.findByStudentClassIdAndAcademicYearId(classId, academicYearId);
    }

}
