package com.example.SpringProject.repository;

import com.example.SpringProject.entity.Course;
import com.example.SpringProject.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseCode(String courseCode);

    List<Course> findByTeacher(Teacher teacher);

    List<Course> findByTeacherId(Long teacherId);

    List<Course> findByCourseNameContainingIgnoreCase(String courseName);

    boolean existsByCourseCode(String courseCode);
}
