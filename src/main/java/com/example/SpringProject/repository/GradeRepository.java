package com.example.SpringProject.repository;

import com.example.SpringProject.entity.Grade;
import com.example.SpringProject.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByEnrollment(Enrollment enrollment);

    List<Grade> findByEnrollmentId(Long enrollmentId);

    List<Grade> findByEnrollmentStudentId(Long studentId);

    List<Grade> findByEnrollmentCourseId(Long courseId);

    List<Grade> findByExamType(String examType);
}
