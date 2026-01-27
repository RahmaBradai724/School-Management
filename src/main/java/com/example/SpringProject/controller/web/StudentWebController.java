package com.example.SpringProject.controller.web;

import com.example.SpringProject.entity.Student;
import com.example.SpringProject.repository.StudentRepository;
import com.example.SpringProject.service.EnrollmentService;
import com.example.SpringProject.service.GradeService;
import com.example.SpringProject.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
@Slf4j
public class StudentWebController {

    private final StudentRepository studentRepository;
    private final EnrollmentService enrollmentService;
    private final ScheduleService scheduleService;
    private final GradeService gradeService;
    private final com.example.SpringProject.service.CourseService courseService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found for username: " + username));

        model.addAttribute("student", student);

        var enrollments = enrollmentService.getEnrollmentsByStudent(student.getId());
        model.addAttribute("enrollments", enrollments);

        // Filter out courses already enrolled in
        var allCourses = courseService.getAllCourses();
        var enrolledCourseIds = enrollments.stream()
                .map(e -> e.getCourse().getId())
                .collect(java.util.stream.Collectors.toSet());
        var availableCourses = allCourses.stream()
                .filter(c -> !enrolledCourseIds.contains(c.getId()))
                .collect(java.util.stream.Collectors.toList());

        model.addAttribute("availableCourses", availableCourses);

        return "student/dashboard";
    }

    @org.springframework.web.bind.annotation.PostMapping("/enroll")
    public String enroll(@org.springframework.web.bind.annotation.RequestParam Long courseId,
            Authentication authentication,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        try {
            enrollmentService.enrollStudent(student.getId(), courseId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Successfully enrolled! A confirmation email has been sent to " + student.getEmail());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/student/dashboard";
    }

    @GetMapping("/courses")
    public String myCourses(Authentication authentication, Model model) {
        String username = authentication.getName();
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        var enrollments = enrollmentService.getEnrollmentsByStudent(student.getId());
        model.addAttribute("student", student);
        model.addAttribute("enrollments", enrollments);
        return "student/courses";
    }

    @GetMapping("/profile")
    public String myProfile(Authentication authentication, Model model) {
        String username = authentication.getName();
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        model.addAttribute("student", student);
        return "student/profile";
    }

    @GetMapping("/schedule")
    public String mySchedule(Authentication authentication, Model model) {
        String username = authentication.getName();
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (student.getStudentGroup() != null) {
            var schedule = scheduleService.getSlotsByGroup(student.getStudentGroup().getId());
            model.addAttribute("schedule", schedule);
        }

        model.addAttribute("student", student);
        return "student/schedule";
    }

    @GetMapping("/grades")
    public String myGrades(Authentication authentication, Model model) {
        String username = authentication.getName();
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        var grades = gradeService.getGradesByStudent(student.getId());
        model.addAttribute("student", student);
        model.addAttribute("grades", grades);
        return "student/grades";
    }
}
