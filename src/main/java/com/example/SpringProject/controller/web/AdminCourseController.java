package com.example.SpringProject.controller.web;

import com.example.SpringProject.entity.Course;
import com.example.SpringProject.service.CourseService;
import com.example.SpringProject.service.TeacherService;
import com.example.SpringProject.service.SpecialtyService;
import com.example.SpringProject.service.StudentGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;
    private final TeacherService teacherService;
    private final SpecialtyService specialtyService;
    private final StudentGroupService groupService;

    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/courses-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("specialties", specialtyService.getAllSpecialties());
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("isEdit", false);
        return "admin/course-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return courseService.getCourseById(id)
                .map(course -> {
                    model.addAttribute("course", course);
                    model.addAttribute("teachers", teacherService.getAllTeachers());
                    model.addAttribute("specialties", specialtyService.getAllSpecialties());
                    model.addAttribute("groups", groupService.getAllGroups());
                    model.addAttribute("isEdit", true);
                    return "admin/course-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Course not found");
                    return "redirect:/admin/courses";
                });
    }

    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        try {
            if (course.getId() == null) {
                courseService.createCourse(course);
                redirectAttributes.addFlashAttribute("success", "Course created successfully");
            } else {
                courseService.updateCourse(course.getId(), course);
                redirectAttributes.addFlashAttribute("success", "Course updated successfully");
            }
            return "redirect:/admin/courses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving course: " + e.getMessage());
            return "redirect:/admin/courses";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("success", "Course deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting course: " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }
}
