package com.example.SpringProject.service;

import com.example.SpringProject.entity.Specialty;
import com.example.SpringProject.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public List<Specialty> getAllSpecialties() {
        return specialtyRepository.findAll();
    }

    public Optional<Specialty> getSpecialtyById(Long id) {
        return specialtyRepository.findById(id);
    }

    public Specialty createSpecialty(Specialty specialty) {
        return specialtyRepository.save(specialty);
    }

    public Specialty updateSpecialty(Long id, Specialty details) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specialty not found"));
        specialty.setName(details.getName());
        specialty.setDescription(details.getDescription());
        return specialtyRepository.save(specialty);
    }

    public void deleteSpecialty(Long id) {
        specialtyRepository.deleteById(id);
    }
}
