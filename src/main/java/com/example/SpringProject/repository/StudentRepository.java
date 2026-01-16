package com.example.SpringProject.repository;

import com.example.SpringProject.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUsername(String username);

    Optional<Student> findByEmail(String email);

    Optional<Student> findByStudentId(String studentId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByStudentId(String studentId);
}
