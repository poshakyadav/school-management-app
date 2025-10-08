package com.spring.st.student_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.st.student_management.model.FeeAllocation;

@Repository
public interface FeeAllocationRepository extends JpaRepository<FeeAllocation, Long> {
    // List<FeeAllocation> findByStudentId(Long studentId);

    @Query("SELECT f FROM FeeAllocation f WHERE f.student.id = :studentId")
    List<FeeAllocation> findByStudentId(Long studentId);

    // @Query("SELECT SUM(f.netAmount) FROM FeeAllocation f WHERE f.student.id =
    // :studentId")
    // Double getTotalAllocatedFeeByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COALESCE(SUM(f.netAmount), 0) FROM FeeAllocation f WHERE f.student.id = :studentId")
    Double getTotalAllocatedFeeByStudentId(@Param("studentId") Long studentId);

    @Modifying
    @Query("DELETE FROM FeeAllocation fa WHERE fa.feeStructure.id = :feeStructureId AND fa.student.studentClass.id = :classId")
    void deleteByFeeStructureAndClassId(@Param("feeStructureId") Long feeStructureId, @Param("classId") Long classId);

    List<FeeAllocation> findByFeeStructureId(Long feeStructureId);

    @Query("SELECT SUM(f.netAmount) FROM FeeAllocation f WHERE f.student.id = :studentId")
    Double getTotalAssignedFeesForStudent(@Param("studentId") Long studentId);

    // Custom query to find FeeAllocation by student ID and fee structure ID
    Optional<FeeAllocation> findByStudentIdAndFeeStructureId(Long studentId, Long feeStructureId);

}
