package com.example.SpringProject.repository;

import com.example.SpringProject.entity.AcademicSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicSessionRepository extends JpaRepository<AcademicSession, Long> {
    Optional<AcademicSession> findByName(String name);

    Optional<AcademicSession> findByActiveTrue();
}
