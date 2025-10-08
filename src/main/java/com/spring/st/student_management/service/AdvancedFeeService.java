package com.spring.st.student_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.AcademicYear;
import com.spring.st.student_management.model.FeeAllocation;
import com.spring.st.student_management.model.FeeStructure;
import com.spring.st.student_management.model.Payment;
import com.spring.st.student_management.model.Student;
import com.spring.st.student_management.repository.FeeAllocationRepository;
import com.spring.st.student_management.repository.FeeStructureRepository;
import com.spring.st.student_management.repository.PaymentRepository;
import com.spring.st.student_management.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class AdvancedFeeService {

    @Autowired
    private FeeStructureRepository feeStructureRepository;

    @Autowired
    private FeeAllocationRepository feeAllocationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StudentRepository studentRepository;

    // Retrieve allocated fees for a student by their ID.

    public List<FeeAllocation> getAllocatedFeesByStudentId(Long studentId) {
        return feeAllocationRepository.findByStudentId(studentId);
    }

    @Transactional
    public void makePayment(Long studentId, Double amount) {
        // Retrieve the Student entity
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        // Create and save the Payment entity
        Payment payment = new Payment();
        payment.setStudent(student);
        payment.setAmount(amount);
        payment.setPaymentDate(new java.util.Date());
        payment.setPaymentMethod("offline"); // Set payment method
        payment.setReceiptNumber("REC-" + System.currentTimeMillis()); // Generate receipt number
        paymentRepository.save(payment);

        // Update the student's payment and outstanding balance details
        double totalPayments = paymentRepository.getTotalPaymentsForStudent(studentId);
        double assignedFees = feeAllocationRepository.getTotalAssignedFeesForStudent(studentId);
        double outstandingBalance = assignedFees - totalPayments;

        // Update the student entity
        student.setTotalPayments(totalPayments);
        student.setOutstandingBalance(outstandingBalance);
        studentRepository.save(student); // Save the updated Student entity

        // Optionally: log or debug the updated balances
        System.out.println("Updated Total Payments: " + totalPayments);
        System.out.println("Updated Outstanding Balance: " + outstandingBalance);
    }

    // Retrieve payment history for a student by their ID.
    public List<Payment> getPaymentHistoryByStudentId(Long studentId) {
        return paymentRepository.findByStudentId(studentId);
    }

    @Transactional
    public void allocateFeesToStudent(Student student, AcademicYear academicYear, double discount) {
        List<FeeStructure> feeStructures = feeStructureRepository
                .findByClassIdAndAcademicYear(student.getStudentClass().getId(), academicYear);

        for (FeeStructure feeStructure : feeStructures) {
            FeeAllocation allocation = new FeeAllocation();
            allocation.setStudent(student);
            allocation.setFeeStructure(feeStructure);
            allocation.setTotalAmount(feeStructure.getAmount());
            allocation.setDiscount(discount);
            allocation.setNetAmount(feeStructure.getAmount() - discount);

            feeAllocationRepository.save(allocation);
        }
    }

    @Transactional
    public void recordPayment(Student student, double amountPaid, String paymentMethod, String receiptNumber) {
        Payment payment = new Payment();
        payment.setStudent(student);
        payment.setPaymentDate(new java.util.Date());
        payment.setAmountPaid(amountPaid);
        payment.setPaymentMethod(paymentMethod);
        payment.setReceiptNumber(receiptNumber);

        paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsForStudent(Long studentId) {
        List<Payment> byStudentId = paymentRepository.findByStudentId(studentId);
        return byStudentId;
    }

    @Transactional
    public void allocateFee(Long studentId, Long feeStructureId, Double discount) {
        // Fetch the student object by ID
        Student student = new Student();
        student.setId(studentId); // Assuming you use a proxy object or fetch from a service

        // Fetch the FeeStructure by ID
        FeeStructure feeStructure = feeStructureRepository.findById(feeStructureId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Fee Structure ID: " + feeStructureId));

        // Calculate net amount after applying the discount
        double totalAmount = feeStructure.getAmount();
        double netAmount = totalAmount - (discount != null ? discount : 0.0);

        // Create a new FeeAllocation record
        FeeAllocation allocation = new FeeAllocation();
        allocation.setStudent(student);
        allocation.setFeeStructure(feeStructure);
        allocation.setTotalAmount(totalAmount);
        allocation.setDiscount(discount != null ? discount : 0.0);
        allocation.setNetAmount(netAmount);

        // Save the FeeAllocation to the database
        feeAllocationRepository.save(allocation);
    }

    public Double getTotalAllocatedFee(Long studentId) {
        return feeAllocationRepository.getTotalAllocatedFeeByStudentId(studentId);
    }

    public Double getTotalPayments(Long studentId) {
        return paymentRepository.getTotalPaymentsByStudentId(studentId);
    }

    public Double calculateOutstanding(Long studentId) {
        Double allocatedFee = getTotalAllocatedFee(studentId);
        Double totalPayments = getTotalPayments(studentId);

        // Use 0.0 if null
        allocatedFee = allocatedFee != null ? allocatedFee : 0.0;
        totalPayments = totalPayments != null ? totalPayments : 0.0;

        return allocatedFee - totalPayments;
    }

    public List<FeeStructure> getFeeStructuresByStudent(Long studentId) {
        // Use the `feeAllocationRepository` to fetch allocations, then map to
        // FeeStructures
        List<FeeAllocation> allocations = feeAllocationRepository.findByStudentId(studentId);
        return allocations.stream()
                .map(FeeAllocation::getFeeStructure)
                .toList(); // Or use `.collect(Collectors.toList());` for Java 8
    }

    public List<FeeStructure> getFeeStructuresForClassAndSection(Long classId, Long sectionId) {
        return feeStructureRepository.findByClassIdAndSectionId(classId, sectionId);
    }

    

}
