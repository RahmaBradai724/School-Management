package com.example.SpringProject.controller.web;

import com.example.SpringProject.entity.Classroom;
import com.example.SpringProject.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/classrooms")
@RequiredArgsConstructor
public class AdminClassroomController {

    private final ClassroomService classroomService;

    @GetMapping
    public String listClassrooms(Model model) {
        model.addAttribute("classrooms", classroomService.getAllClassrooms());
        return "admin/classrooms-list";
    }

    @GetMapping("/new")
    public String newClassroomForm(Model model) {
        model.addAttribute("classroom", new Classroom());
        return "admin/classroom-form";
    }

    @PostMapping
    public String saveClassroom(@ModelAttribute Classroom classroom) {
        classroomService.createClassroom(classroom);
        return "redirect:/admin/classrooms";
    }

    @GetMapping("/delete/{id}")
    public String deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return "redirect:/admin/classrooms";
    }
}
