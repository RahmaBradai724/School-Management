package com.example.SpringProject.repository;

import com.example.SpringProject.entity.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {
    List<StudentGroup> findBySpecialtyId(Long specialtyId);

    List<StudentGroup> findByAcademicSessionId(Long academicSessionId);
}
