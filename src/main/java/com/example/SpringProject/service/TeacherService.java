package com.example.SpringProject.service;

import com.example.SpringProject.entity.Teacher;
import com.example.SpringProject.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public Optional<Teacher> getTeacherByUsername(String username) {
        return teacherRepository.findByUsername(username);
    }

    public Optional<Teacher> getTeacherByTeacherId(String teacherId) {
        return teacherRepository.findByTeacherId(teacherId);
    }

    public List<Teacher> getTeachersBySpecialization(String specialization) {
        return teacherRepository.findBySpecialization(specialization);
    }

    public Teacher createTeacher(Teacher teacher) {
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        return teacherRepository.save(teacher);
    }

    public Teacher updateTeacher(Long id, Teacher teacherDetails) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));

        teacher.setFirstName(teacherDetails.getFirstName());
        teacher.setLastName(teacherDetails.getLastName());
        teacher.setEmail(teacherDetails.getEmail());
        teacher.setTeacherId(teacherDetails.getTeacherId());
        teacher.setSpecialization(teacherDetails.getSpecialization());

        if (teacherDetails.getPassword() != null && !teacherDetails.getPassword().isEmpty()) {
            teacher.setPassword(passwordEncoder.encode(teacherDetails.getPassword()));
        }

        return teacherRepository.save(teacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return teacherRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return teacherRepository.existsByEmail(email);
    }
}
