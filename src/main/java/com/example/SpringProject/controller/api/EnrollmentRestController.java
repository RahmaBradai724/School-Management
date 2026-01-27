package com.example.SpringProject.controller.api;

import com.example.SpringProject.entity.Enrollment;
import com.example.SpringProject.entity.EnrollmentStatus;
import com.example.SpringProject.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@CrossOrigin
public class EnrollmentRestController {

    private final EnrollmentService enrollmentService;
    private final com.example.SpringProject.repository.StudentRepository studentRepository;

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    @PostMapping("/self/enroll")
    public ResponseEntity<?> enrollSelf(@RequestParam Long courseId,
            org.springframework.security.core.Authentication authentication) {
        String username = authentication.getName();
        return studentRepository.findByUsername(username)
                .map(student -> {
                    try {
                        Enrollment enrollment = enrollmentService.enrollStudent(student.getId(), courseId);
                        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
                    } catch (RuntimeException e) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).body("Student profile not found"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStatus(@PathVariable EnrollmentStatus status) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStatus(status));
    }

    @PostMapping("/enroll")
    public ResponseEntity<?> enrollStudent(@RequestParam Long studentId, @RequestParam Long courseId) {
        try {
            Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseId);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Enrollment> updateEnrollmentStatus(@PathVariable Long id,
            @RequestParam EnrollmentStatus status) {
        try {
            Enrollment updatedEnrollment = enrollmentService.updateEnrollmentStatus(id, status);
            return ResponseEntity.ok(updatedEnrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        try {
            enrollmentService.deleteEnrollment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
