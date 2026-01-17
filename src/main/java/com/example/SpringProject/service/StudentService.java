package com.example.SpringProject.service;

import com.example.SpringProject.entity.Student;
import com.example.SpringProject.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByUsername(String username) {
        return studentRepository.findByUsername(username);
    }

    public Optional<Student> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    public Student createStudent(Student student) {
        // Encode password before saving
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        if (student.getRole() == null) {
            student.setRole(com.example.SpringProject.entity.UserRole.STUDENT);
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setEmail(studentDetails.getEmail());
        student.setStudentId(studentDetails.getStudentId());
        student.setEnrollmentDate(studentDetails.getEnrollmentDate());
        student.setStudentGroup(studentDetails.getStudentGroup());

        // Only update password if a new one is provided
        if (studentDetails.getPassword() != null && !studentDetails.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(studentDetails.getPassword()));
        }

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return studentRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }
}
