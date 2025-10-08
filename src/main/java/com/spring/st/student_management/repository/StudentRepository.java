package com.spring.st.student_management.repository;

import org.springframework.stereotype.Repository;

import com.spring.st.student_management.model.AcademicYear;
import com.spring.st.student_management.model.Student;
import com.spring.st.student_management.model.StudentClass;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByStudentClass(StudentClass studentClass);

    // Double findOutstandingBalanceByStudentId(Long id);

    @Query("SELECT s.outstandingBalance FROM Student s WHERE s.id = :studentId")
    Double findOutstandingBalanceByStudentId(@Param("studentId") Long studentId);

    List<Student> findByStudentClassId(Long classId);

    List<Student> findByStudentClassAndAcademicYear(StudentClass studentClass, AcademicYear academicYear);

    List<Student> findByAcademicYear(AcademicYear academicYear);

    List<Student> findByStudentClassIdAndAcademicYearId(Long studentClassId, Long academicYearId);

    // @Query("SELECT s FROM Student s WHERE s.studentClass.id = :classId AND s.academicYear.id = :academicYearId")
    // List<Student> findByStudentClassIdAndAcademicYearId(
    //         @Param("classId") Long classId,
    //         @Param("academicYearId") Long academicYearId);

}
