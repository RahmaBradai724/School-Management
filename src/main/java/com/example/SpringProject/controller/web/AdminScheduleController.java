package com.example.SpringProject.controller.web;

import com.example.SpringProject.entity.ScheduleSlot;
import com.example.SpringProject.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;

@Controller
@RequestMapping("/admin/schedule")
@RequiredArgsConstructor
public class AdminScheduleController {

    private final ScheduleService scheduleService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;
    private final StudentGroupService groupService;

    @GetMapping
    public String viewSchedule(Model model) {
        model.addAttribute("slots", scheduleService.getAllSlots());
        return "admin/schedule-list";
    }

    @GetMapping("/add")
    public String addSlotForm(Model model) {
        model.addAttribute("newSlot", new ScheduleSlot());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("classrooms", classroomService.getAllClassrooms());
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("days", DayOfWeek.values());
        return "admin/schedule-form";
    }

    @PostMapping("/add")
    public String saveSlot(@ModelAttribute ScheduleSlot slot, Model model) {
        try {
            scheduleService.createScheduleSlot(slot);
            return "redirect:/admin/schedule?success=Schedule slot added";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("newSlot", slot);
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("teachers", teacherService.getAllTeachers());
            model.addAttribute("classrooms", classroomService.getAllClassrooms());
            model.addAttribute("groups", groupService.getAllGroups());
            model.addAttribute("days", DayOfWeek.values());
            return "admin/schedule-form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteSlot(@PathVariable Long id) {
        scheduleService.deleteSlot(id);
        return "redirect:/admin/schedule?success=Slot deleted";
    }
}
