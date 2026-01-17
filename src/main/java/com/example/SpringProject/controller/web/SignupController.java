package com.example.SpringProject.controller.web;

import com.example.SpringProject.entity.Student;
import com.example.SpringProject.service.StudentService;
import com.example.SpringProject.service.EmailService;
import com.example.SpringProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {

    private final StudentService studentService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @GetMapping
    public String signupForm(Model model) {
        model.addAttribute("student", new Student());
        return "signup";
    }

    @PostMapping
    public String registerStudent(@ModelAttribute Student student, Model model) {
        if (userRepository.existsByUsername(student.getUsername())) {
            model.addAttribute("error", "Username already exists");
            return "signup";
        }
        if (userRepository.existsByEmail(student.getEmail())) {
            model.addAttribute("error", "Email already exists");
            return "signup";
        }

        student.setEnrollmentDate(LocalDate.now());

        // Generate a studentId if not provided (for self-registration)
        if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
            student.setStudentId("STU" + System.currentTimeMillis());
        }

        try {
            studentService.createStudent(student);
            emailService.sendEmail(student.getEmail(), "Welcome to School Management System",
                    "Dear " + student.getFirstName()
                            + ",\n\nYour account has been successfully created. Your student ID is: "
                            + student.getStudentId() + ".\n\nYou can now login to your portal.");
        } catch (Exception e) {
            model.addAttribute("error", "Error creating account: " + e.getMessage());
            return "signup";
        }

        return "redirect:/login?success=Registration successful! Please login.";
    }
}
