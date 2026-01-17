package com.example.SpringProject.service;

import com.example.SpringProject.entity.ScheduleSlot;
import com.example.SpringProject.repository.ClassroomRepository;
import com.example.SpringProject.repository.ScheduleSlotRepository;
import com.example.SpringProject.repository.StudentGroupRepository;
import com.example.SpringProject.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final ScheduleSlotRepository scheduleSlotRepository;
    private final TeacherRepository teacherRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final ClassroomRepository classroomRepository;

    public List<ScheduleSlot> getAllSlots() {
        return scheduleSlotRepository.findAll();
    }

    public List<ScheduleSlot> getSlotsByGroup(Long groupId) {
        return scheduleSlotRepository.findByStudentGroupId(groupId);
    }

    public List<ScheduleSlot> getSlotsByTeacher(Long teacherId) {
        return scheduleSlotRepository.findByTeacherId(teacherId);
    }

    public ScheduleSlot createScheduleSlot(ScheduleSlot slot) {
        // Fetch full entities to ensure we have names for error messages
        if (slot.getTeacher() != null && slot.getTeacher().getId() != null) {
            slot.setTeacher(teacherRepository.findById(slot.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found")));
        }
        if (slot.getStudentGroup() != null && slot.getStudentGroup().getId() != null) {
            slot.setStudentGroup(studentGroupRepository.findById(slot.getStudentGroup().getId())
                    .orElseThrow(() -> new RuntimeException("Student Group not found")));
        }
        if (slot.getClassroom() != null && slot.getClassroom().getId() != null) {
            slot.setClassroom(classroomRepository.findById(slot.getClassroom().getId())
                    .orElseThrow(() -> new RuntimeException("Classroom not found")));
        }

        validateConflicts(slot);
        return scheduleSlotRepository.save(slot);
    }

    private void validateConflicts(ScheduleSlot newSlot) {
        // Teacher conflicts
        List<ScheduleSlot> teacherSlots = scheduleSlotRepository.findByDayAndTeacher(newSlot.getDayOfWeek(),
                newSlot.getTeacher().getId());
        for (ScheduleSlot existing : teacherSlots) {
            if (isOverlapping(newSlot, existing)) {
                throw new RuntimeException(
                        "Teacher " + newSlot.getTeacher().getFirstName() + " already has a course at this time.");
            }
        }

        // Student Group conflicts
        List<ScheduleSlot> groupSlots = scheduleSlotRepository.findByDayAndGroup(newSlot.getDayOfWeek(),
                newSlot.getStudentGroup().getId());
        for (ScheduleSlot existing : groupSlots) {
            if (isOverlapping(newSlot, existing)) {
                throw new RuntimeException(
                        "Student Group " + newSlot.getStudentGroup().getName() + " already has a course at this time.");
            }
        }

        // Classroom conflicts
        List<ScheduleSlot> roomSlots = scheduleSlotRepository.findByDayAndClassroom(newSlot.getDayOfWeek(),
                newSlot.getClassroom().getId());
        for (ScheduleSlot existing : roomSlots) {
            if (isOverlapping(newSlot, existing)) {
                throw new RuntimeException(
                        "Classroom " + newSlot.getClassroom().getName() + " is already occupied at this time.");
            }
        }
    }

    private boolean isOverlapping(ScheduleSlot s1, ScheduleSlot s2) {
        LocalTime start1 = s1.getStartTime();
        LocalTime end1 = s1.getEndTime();
        LocalTime start2 = s2.getStartTime();
        LocalTime end2 = s2.getEndTime();

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public void deleteSlot(Long id) {
        scheduleSlotRepository.deleteById(id);
    }
}
