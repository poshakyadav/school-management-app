package com.spring.st.student_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.st.student_management.model.AcademicYear;
import com.spring.st.student_management.model.FeeStructure;

@Repository
public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {
    List<FeeStructure> findByClassIdAndAcademicYear(Long classId, AcademicYear academicYear);

    // List<FeeStructure> findByClassId(Long classId);

    // List<String> findAssignedClasses(Long feeStructureId);

    @Query("SELECT sc.name FROM StudentClass sc JOIN sc.feeStructures fs WHERE fs.id = :feeStructureId")
    List<String> findAssignedClasses(Long feeStructureId);

    @Query("SELECT f FROM FeeStructure f JOIN FETCH f.assignedClasses")
    List<FeeStructure> findAllWithClasses();

    // List<FeeStructure> findByClassIdAndSectionId(Long classId, Long sectionId);

    @Query("SELECT f FROM FeeStructure f " +
            "JOIN f.assignedClasses sc " +
            "JOIN sc.sections s " +
            "WHERE sc.id = :classId AND s.id = :sectionId")
    List<FeeStructure> findByClassIdAndSectionId(@Param("classId") Long classId, @Param("sectionId") Long sectionId);


    @Query("SELECT f FROM FeeStructure f WHERE f.classId = :classId AND f.academicYear.id = :academicYearId AND f.status = 'Active'")
    List<FeeStructure> findByClassIdAndAcademicYearId(@Param("classId") Long classId, @Param("academicYearId") Long academicYearId);


}