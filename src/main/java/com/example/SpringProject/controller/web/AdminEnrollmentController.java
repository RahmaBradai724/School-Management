package com.example.SpringProject.controller.web;

import com.example.SpringProject.service.EnrollmentService;
import com.example.SpringProject.service.StudentService;
import com.example.SpringProject.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/enrollments")
@RequiredArgsConstructor
public class AdminEnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping
    public String listEnrollments(Model model) {
        model.addAttribute("enrollments", enrollmentService.getAllEnrollments());
        return "admin/enrollments-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/enrollment-form";
    }

    @PostMapping("/enroll")
    public String enrollStudent(@RequestParam Long studentId,
            @RequestParam Long courseId,
            RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.enrollStudent(studentId, courseId);
            redirectAttributes.addFlashAttribute("success", "Student enrolled successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error enrolling student: " + e.getMessage());
        }
        return "redirect:/admin/enrollments";
    }

    @GetMapping("/delete/{id}")
    public String deleteEnrollment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.deleteEnrollment(id);
            redirectAttributes.addFlashAttribute("success", "Enrollment deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting enrollment: " + e.getMessage());
        }
        return "redirect:/admin/enrollments";
    }
}
