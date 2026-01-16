package com.example.SpringProject.controller.api;

import com.example.SpringProject.entity.Teacher;
import com.example.SpringProject.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@CrossOrigin
public class TeacherRestController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<Teacher>> getTeachersBySpecialization(@PathVariable String specialization) {
        return ResponseEntity.ok(teacherService.getTeachersBySpecialization(specialization));
    }

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
        try {
            Teacher createdTeacher = teacherService.createTeacher(teacher);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        try {
            Teacher updatedTeacher = teacherService.updateTeacher(id, teacher);
            return ResponseEntity.ok(updatedTeacher);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        try {
            teacherService.deleteTeacher(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
