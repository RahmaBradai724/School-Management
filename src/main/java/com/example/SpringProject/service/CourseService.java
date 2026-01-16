package com.example.SpringProject.service;

import com.example.SpringProject.entity.Course;
import com.example.SpringProject.entity.Teacher;
import com.example.SpringProject.repository.CourseRepository;
import com.example.SpringProject.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Optional<Course> getCourseByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }

    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public List<Course> searchCoursesByName(String courseName) {
        return courseRepository.findByCourseNameContainingIgnoreCase(courseName);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        course.setCourseCode(courseDetails.getCourseCode());
        course.setCourseName(courseDetails.getCourseName());
        course.setDescription(courseDetails.getDescription());
        course.setCredits(courseDetails.getCredits());
        course.setTeacher(courseDetails.getTeacher());

        return courseRepository.save(course);
    }

    public Course assignTeacherToCourse(Long courseId, Long teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public boolean existsByCourseCode(String courseCode) {
        return courseRepository.existsByCourseCode(courseCode);
    }
}
