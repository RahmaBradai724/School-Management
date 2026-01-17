package com.example.SpringProject.controller.web;

import com.example.SpringProject.entity.Teacher;
import com.example.SpringProject.repository.TeacherRepository;
import com.example.SpringProject.service.CourseService;
import com.example.SpringProject.service.GradeService;
import com.example.SpringProject.service.EnrollmentService;
import com.example.SpringProject.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherWebController {

        private final TeacherRepository teacherRepository;
        private final CourseService courseService;
        private final EnrollmentService enrollmentService;
        private final GradeService gradeService;
        private final ScheduleService scheduleService;

        @GetMapping("/dashboard")
        public String dashboard(Authentication authentication, Model model) {
                String username = authentication.getName();
                Teacher teacher = teacherRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Teacher not found"));

                model.addAttribute("teacher", teacher);
                model.addAttribute("courses", courseService.getCoursesByTeacher(teacher.getId()));
                return "teacher/dashboard";
        }

        @GetMapping("/students")
        public String myStudents(Authentication authentication, Model model) {
                String username = authentication.getName();
                Teacher teacher = teacherRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Teacher not found"));

                var courses = courseService.getCoursesByTeacher(teacher.getId());
                var enrollments = courses.stream()
                                .flatMap(course -> enrollmentService.getEnrollmentsByCourse(course.getId()).stream())
                                .collect(Collectors.toList());

                model.addAttribute("teacher", teacher);
                model.addAttribute("enrollments", enrollments);
                return "teacher/students";
        }

        @GetMapping("/grades")
        public String gradeEntry(Authentication authentication, Model model) {
                String username = authentication.getName();
                Teacher teacher = teacherRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Teacher not found"));

                var courses = courseService.getCoursesByTeacher(teacher.getId());
                var enrollments = courses.stream()
                                .flatMap(course -> enrollmentService.getEnrollmentsByCourse(course.getId()).stream())
                                .collect(Collectors.toList());

                model.addAttribute("teacher", teacher);
                model.addAttribute("enrollments", enrollments);
                return "teacher/grades";
        }

        @GetMapping("/grades/add/{id}")
        public String addGradeForm(@PathVariable Long id, Authentication authentication, Model model) {
                var enrollment = enrollmentService.getEnrollmentById(id)
                                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

                model.addAttribute("enrollment", enrollment);
                return "teacher/grade-form";
        }

        @PostMapping("/grades/add")
        public String saveGrade(@RequestParam Long enrollmentId,
                        @RequestParam String examType,
                        @RequestParam Double score,
                        @RequestParam String comments) {
                gradeService.addGrade(enrollmentId, examType, score, comments);
                return "redirect:/teacher/grades?success=Grade added successfully";
        }

        @GetMapping("/schedule")
        public String mySchedule(Authentication authentication, Model model) {
                String username = authentication.getName();
                Teacher teacher = teacherRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Teacher not found"));

                var schedule = scheduleService.getSlotsByTeacher(teacher.getId());
                model.addAttribute("teacher", teacher);
                model.addAttribute("schedule", schedule);
                return "teacher/schedule";
        }
}
