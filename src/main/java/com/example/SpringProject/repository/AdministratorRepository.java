package com.example.SpringProject.repository;

import com.example.SpringProject.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

    Optional<Administrator> findByUsername(String username);

    Optional<Administrator> findByEmail(String email);

    Optional<Administrator> findByAdminId(String adminId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByAdminId(String adminId);
}
