package com.example.SpringProject.service;

import com.example.SpringProject.entity.StudentGroup;
import com.example.SpringProject.repository.StudentGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentGroupService {

    private final StudentGroupRepository studentGroupRepository;

    public List<StudentGroup> getAllGroups() {
        return studentGroupRepository.findAll();
    }

    public Optional<StudentGroup> getGroupById(Long id) {
        return studentGroupRepository.findById(id);
    }

    public List<StudentGroup> getGroupsBySpecialty(Long specialtyId) {
        return studentGroupRepository.findBySpecialtyId(specialtyId);
    }

    public StudentGroup createGroup(StudentGroup group) {
        return studentGroupRepository.save(group);
    }

    public StudentGroup updateGroup(Long id, StudentGroup details) {
        StudentGroup group = studentGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        group.setName(details.getName());
        group.setSpecialty(details.getSpecialty());
        group.setAcademicSession(details.getAcademicSession());
        return studentGroupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        studentGroupRepository.deleteById(id);
    }
}
