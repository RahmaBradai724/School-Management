package com.example.SpringProject.controller.web;

import com.example.SpringProject.entity.AcademicSession;
import com.example.SpringProject.entity.Specialty;
import com.example.SpringProject.entity.StudentGroup;
import com.example.SpringProject.service.AcademicSessionService;
import com.example.SpringProject.service.SpecialtyService;
import com.example.SpringProject.service.StudentGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminManagementController {

    private final AcademicSessionService sessionService;
    private final SpecialtyService specialtyService;
    private final StudentGroupService groupService;

    // --- Academic Sessions ---
    @GetMapping("/sessions")
    public String listSessions(Model model) {
        model.addAttribute("sessions", sessionService.getAllSessions());
        model.addAttribute("newSession", new AcademicSession());
        return "admin/sessions";
    }

    @PostMapping("/sessions")
    public String saveSession(@ModelAttribute AcademicSession session) {
        sessionService.createSession(session);
        return "redirect:/admin/sessions?success=Session saved";
    }

    @GetMapping("/sessions/delete/{id}")
    public String deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return "redirect:/admin/sessions?success=Session deleted";
    }

    // --- Specialties ---
    @GetMapping("/specialties")
    public String listSpecialties(Model model) {
        model.addAttribute("specialties", specialtyService.getAllSpecialties());
        model.addAttribute("newSpecialty", new Specialty());
        return "admin/specialties";
    }

    @PostMapping("/specialties")
    public String saveSpecialty(@ModelAttribute Specialty specialty) {
        specialtyService.createSpecialty(specialty);
        return "redirect:/admin/specialties?success=Specialty saved";
    }

    @GetMapping("/specialties/delete/{id}")
    public String deleteSpecialty(@PathVariable Long id) {
        specialtyService.deleteSpecialty(id);
        return "redirect:/admin/specialties?success=Specialty deleted";
    }

    // --- Student Groups ---
    @GetMapping("/groups")
    public String listGroups(Model model) {
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("specialties", specialtyService.getAllSpecialties());
        model.addAttribute("sessions", sessionService.getAllSessions());
        model.addAttribute("newGroup", new StudentGroup());
        return "admin/groups";
    }

    @PostMapping("/groups")
    public String saveGroup(@ModelAttribute StudentGroup group) {
        groupService.createGroup(group);
        return "redirect:/admin/groups?success=Group saved";
    }

    @GetMapping("/groups/delete/{id}")
    public String deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return "redirect:/admin/groups?success=Group deleted";
    }
}
