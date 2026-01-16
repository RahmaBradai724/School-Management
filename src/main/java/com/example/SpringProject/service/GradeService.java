package com.example.SpringProject.service;

import com.example.SpringProject.entity.Grade;
import com.example.SpringProject.entity.Enrollment;
import com.example.SpringProject.repository.GradeRepository;
import com.example.SpringProject.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GradeService {

    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Optional<Grade> getGradeById(Long id) {
        return gradeRepository.findById(id);
    }

    public List<Grade> getGradesByEnrollment(Long enrollmentId) {
        return gradeRepository.findByEnrollmentId(enrollmentId);
    }

    public List<Grade> getGradesByStudent(Long studentId) {
        return gradeRepository.findByEnrollmentStudentId(studentId);
    }

    public List<Grade> getGradesByCourse(Long courseId) {
        return gradeRepository.findByEnrollmentCourseId(courseId);
    }

    public Grade addGrade(Long enrollmentId, String examType, double score, String comments) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));

        Grade grade = new Grade();
        grade.setEnrollment(enrollment);
        grade.setExamType(examType);
        grade.setScore(score);
        grade.setDateRecorded(LocalDate.now());
        grade.setComments(comments);

        return gradeRepository.save(grade);
    }

    public Grade updateGrade(Long id, Grade gradeDetails) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + id));

        grade.setExamType(gradeDetails.getExamType());
        grade.setScore(gradeDetails.getScore());
        grade.setComments(gradeDetails.getComments());

        return gradeRepository.save(grade);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }

    public double calculateAverageForEnrollment(Long enrollmentId) {
        List<Grade> grades = gradeRepository.findByEnrollmentId(enrollmentId);

        if (grades.isEmpty()) {
            return 0.0;
        }

        return grades.stream()
                .mapToDouble(Grade::getScore)
                .average()
                .orElse(0.0);
    }
}
