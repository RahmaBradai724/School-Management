package com.example.SpringProject.service;

import com.example.SpringProject.entity.Grade;
import com.example.SpringProject.entity.Student;
import com.example.SpringProject.repository.EnrollmentRepository;
import com.example.SpringProject.repository.GradeRepository;
import com.example.SpringProject.repository.StudentRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

    public Double calculateStudentGPA(Long studentId) {
        List<Grade> grades = gradeRepository.findByEnrollmentStudentId(studentId);
        if (grades.isEmpty())
            return 0.0;

        double totalPoints = 0;
        int totalCredits = 0;

        for (Grade grade : grades) {
            int credits = grade.getEnrollment().getCourse().getCredits();
            totalPoints += grade.getScore() * credits;
            totalCredits += credits;
        }

        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }

    public Double calculateCourseSuccessRate(Long courseId) {
        List<Grade> grades = gradeRepository.findByEnrollmentCourseId(courseId);
        if (grades.isEmpty())
            return 0.0;

        long passingGrades = grades.stream()
                .filter(g -> g.getScore() >= 10.0) // Assuming 10 is the passing score
                .count();

        return (double) passingGrades / grades.size() * 100;
    }

    public Map<String, Long> getMostFollowedCourses() {
        return enrollmentRepository.findAll().stream()
                .collect(Collectors.groupingBy(e -> e.getCourse().getCourseName(), Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public byte[] generateGradeReportPDF(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Grade> grades = gradeRepository.findByEnrollmentStudentId(studentId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Grade Report for " + student.getFirstName() + " " + student.getLastName())
                    .setFontSize(20).setBold());
            document.add(new Paragraph("Student ID: " + student.getStudentId()));
            document.add(new Paragraph("Current GPA: " + String.format("%.2f", calculateStudentGPA(studentId))));
            document.add(new Paragraph("\n"));

            Table table = new Table(3);
            table.addCell("Course");
            table.addCell("Type");
            table.addCell("Score");

            for (Grade grade : grades) {
                table.addCell(grade.getEnrollment().getCourse().getCourseName());
                table.addCell(grade.getExamType());
                table.addCell(String.valueOf(grade.getScore()));
            }

            document.add(table);
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
