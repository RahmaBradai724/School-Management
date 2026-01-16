package com.example.SpringProject.controller.web;

import com.example.SpringProject.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("teacherCount", teacherService.getAllTeachers().size());
        model.addAttribute("courseCount", courseService.getAllCourses().size());
        model.addAttribute("enrollmentCount", enrollmentService.getAllEnrollments().size());
        return "admin/dashboard";
    }
}
