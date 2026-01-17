package com.example.SpringProject.service;

import com.example.SpringProject.entity.Classroom;
import com.example.SpringProject.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    public Optional<Classroom> getClassroomById(Long id) {
        return classroomRepository.findById(id);
    }

    public Classroom createClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    public Classroom updateClassroom(Long id, Classroom details) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        classroom.setName(details.getName());
        classroom.setCapacity(details.getCapacity());
        classroom.setBuilding(details.getBuilding());
        return classroomRepository.save(classroom);
    }

    public void deleteClassroom(Long id) {
        classroomRepository.deleteById(id);
    }
}
