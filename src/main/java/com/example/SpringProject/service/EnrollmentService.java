package com.example.SpringProject.service;

import com.example.SpringProject.entity.Enrollment;
import com.example.SpringProject.entity.EnrollmentStatus;
import com.example.SpringProject.entity.Student;
import com.example.SpringProject.entity.Course;
import com.example.SpringProject.repository.EnrollmentRepository;
import com.example.SpringProject.repository.StudentRepository;
import com.example.SpringProject.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EmailService emailService;

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Optional<Enrollment> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id);
    }

    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public List<Enrollment> getEnrollmentsByStatus(EnrollmentStatus status) {
        return enrollmentRepository.findByStatus(status);
    }

    public Enrollment enrollStudent(Long studentId, Long courseId) {
        // Check if already enrolled
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Notify Student
        emailService.sendEmail(student.getEmail(), "Course Enrollment Successful",
                "Dear " + student.getFirstName() + ",\n\nYou have been successfully enrolled in the course: "
                        + course.getCourseName() + " (" + course.getCourseCode() + ").");

        // Notify Teacher
        if (course.getTeacher() != null) {
            emailService.sendEmail(course.getTeacher().getEmail(), "New Student Enrolled",
                    "Dear " + course.getTeacher().getFirstName() + ",\n\nA new student (" + student.getFirstName() + " "
                            + student.getLastName() + ") has enrolled in your course: " + course.getCourseName() + ".");
        }

        return savedEnrollment;
    }

    public Enrollment updateEnrollmentStatus(Long enrollmentId, EnrollmentStatus status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));

        enrollment.setStatus(status);
        return enrollmentRepository.save(enrollment);
    }

    public void deleteEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollmentRepository.deleteById(id);

        // Notify Student & Teacher of unenrollment
        emailService.sendEmail(enrollment.getStudent().getEmail(), "Course Unenrollment",
                "You have been unenrolled from the course: " + enrollment.getCourse().getCourseName() + ".");

        if (enrollment.getCourse().getTeacher() != null) {
            emailService.sendEmail(enrollment.getCourse().getTeacher().getEmail(), "Student Unenrolled",
                    "A student has unenrolled from your course: " + enrollment.getCourse().getCourseName() + ".");
        }
    }
}
