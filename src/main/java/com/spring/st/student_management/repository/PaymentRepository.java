package com.spring.st.student_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.st.student_management.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // List<Payment> findByStudentId(Long studentId);

    @Query("SELECT p FROM Payment p WHERE p.student.id = :studentId")
    List<Payment> findByStudentId(Long studentId);

    @Query("SELECT COALESCE(SUM(p.amountPaid), 0) FROM Payment p WHERE p.student.id = :studentId")
    Double getTotalPaymentsByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.student.id = :studentId")
    Double getTotalPaymentsForStudent(@Param("studentId") Long studentId);

}
