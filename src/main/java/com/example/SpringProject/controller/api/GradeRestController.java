package com.example.SpringProject.controller.api;

import com.example.SpringProject.entity.Grade;
import com.example.SpringProject.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
@CrossOrigin
public class GradeRestController {

    private final GradeService gradeService;

    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        return ResponseEntity.ok(gradeService.getAllGrades());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        return gradeService.getGradeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.getGradesByStudent(studentId));
    }

    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<List<Grade>> getGradesByEnrollment(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(gradeService.getGradesByEnrollment(enrollmentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(gradeService.getGradesByCourse(courseId));
    }

    @GetMapping("/enrollment/{enrollmentId}/average")
    public ResponseEntity<Map<String, Double>> getAverageGrade(@PathVariable Long enrollmentId) {
        double average = gradeService.calculateAverageForEnrollment(enrollmentId);
        return ResponseEntity.ok(Map.of("average", average));
    }

    @PostMapping
    public ResponseEntity<?> addGrade(@RequestParam Long enrollmentId,
            @RequestParam String examType,
            @RequestParam double score,
            @RequestParam(required = false) String comments) {
        try {
            Grade grade = gradeService.addGrade(enrollmentId, examType, score, comments);
            return ResponseEntity.status(HttpStatus.CREATED).body(grade);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody Grade grade) {
        try {
            Grade updatedGrade = gradeService.updateGrade(id, grade);
            return ResponseEntity.ok(updatedGrade);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        try {
            gradeService.deleteGrade(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
