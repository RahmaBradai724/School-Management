package com.example.SpringProject.controller.web;

import com.example.SpringProject.entity.Student;
import com.example.SpringProject.service.StudentService;
import com.example.SpringProject.service.StudentGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentService studentService;
    private final StudentGroupService groupService;

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "admin/students-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("isEdit", false);
        return "admin/student-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return studentService.getStudentById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    model.addAttribute("groups", groupService.getAllGroups());
                    model.addAttribute("isEdit", true);
                    return "admin/student-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Student not found");
                    return "redirect:/admin/students";
                });
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        try {
            if (student.getId() == null) {
                studentService.createStudent(student);
                redirectAttributes.addFlashAttribute("success", "Student created successfully");
            } else {
                studentService.updateStudent(student.getId(), student);
                redirectAttributes.addFlashAttribute("success", "Student updated successfully");
            }
            return "redirect:/admin/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving student: " + e.getMessage());
            return "redirect:/admin/students";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("success", "Student deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting student: " + e.getMessage());
        }
        return "redirect:/admin/students";
    }
}
