package com.example.SpringProject.service;

import com.example.SpringProject.entity.Administrator;
import com.example.SpringProject.repository.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Administrator> getAllAdministrators() {
        return administratorRepository.findAll();
    }

    public Optional<Administrator> getAdministratorById(Long id) {
        return administratorRepository.findById(id);
    }

    public Optional<Administrator> getAdministratorByUsername(String username) {
        return administratorRepository.findByUsername(username);
    }

    public Administrator createAdministrator(Administrator administrator) {
        administrator.setPassword(passwordEncoder.encode(administrator.getPassword()));
        return administratorRepository.save(administrator);
    }

    public Administrator updateAdministrator(Long id, Administrator adminDetails) {
        Administrator admin = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found with id: " + id));

        admin.setFirstName(adminDetails.getFirstName());
        admin.setLastName(adminDetails.getLastName());
        admin.setEmail(adminDetails.getEmail());
        admin.setAdminId(adminDetails.getAdminId());
        admin.setDepartment(adminDetails.getDepartment());

        if (adminDetails.getPassword() != null && !adminDetails.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(adminDetails.getPassword()));
        }

        return administratorRepository.save(admin);
    }

    public void deleteAdministrator(Long id) {
        administratorRepository.deleteById(id);
    }
}
