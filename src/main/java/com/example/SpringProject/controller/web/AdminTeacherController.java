package com.example.SpringProject.controller.web;

import com.example.SpringProject.entity.Teacher;
import com.example.SpringProject.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/teachers")
@RequiredArgsConstructor
public class AdminTeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public String listTeachers(Model model) {
        model.addAttribute("teachers", teacherService.getAllTeachers());
        return "admin/teachers-list";
    }

    @GetMapping("/new")
    public String newTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("isEdit", false);
        return "admin/teacher-form";
    }

    @PostMapping
    public String saveTeacher(@ModelAttribute Teacher teacher) {
        if (teacher.getId() != null) {
            teacherService.updateTeacher(teacher.getId(), teacher);
        } else {
            teacherService.createTeacher(teacher);
        }
        return "redirect:/admin/teachers";
    }

    @GetMapping("/edit/{id}")
    public String editTeacherForm(@PathVariable Long id, Model model) {
        Teacher teacher = teacherService.getTeacherById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        model.addAttribute("teacher", teacher);
        model.addAttribute("isEdit", true);
        return "admin/teacher-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return "redirect:/admin/teachers";
    }
}
