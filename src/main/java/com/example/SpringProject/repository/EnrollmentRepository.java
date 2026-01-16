package com.example.SpringProject.repository;

import com.example.SpringProject.entity.Enrollment;
import com.example.SpringProject.entity.EnrollmentStatus;
import com.example.SpringProject.entity.Student;
import com.example.SpringProject.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudent(Student student);

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourse(Course course);

    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
