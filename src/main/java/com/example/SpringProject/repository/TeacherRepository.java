package com.example.SpringProject.repository;

import com.example.SpringProject.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUsername(String username);

    Optional<Teacher> findByEmail(String email);

    Optional<Teacher> findByTeacherId(String teacherId);

    List<Teacher> findBySpecialization(String specialization);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByTeacherId(String teacherId);
}
