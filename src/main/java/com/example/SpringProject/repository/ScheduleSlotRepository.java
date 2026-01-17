package com.example.SpringProject.repository;

import com.example.SpringProject.entity.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long> {

    List<ScheduleSlot> findByTeacherId(Long teacherId);

    List<ScheduleSlot> findByStudentGroupId(Long groupId);

    List<ScheduleSlot> findByClassroomId(Long classroomId);

    List<ScheduleSlot> findByDayOfWeek(DayOfWeek dayOfWeek);

    @Query("SELECT s FROM ScheduleSlot s WHERE s.dayOfWeek = :day AND s.teacher.id = :teacherId")
    List<ScheduleSlot> findByDayAndTeacher(DayOfWeek day, Long teacherId);

    @Query("SELECT s FROM ScheduleSlot s WHERE s.dayOfWeek = :day AND s.studentGroup.id = :groupId")
    List<ScheduleSlot> findByDayAndGroup(DayOfWeek day, Long groupId);

    @Query("SELECT s FROM ScheduleSlot s WHERE s.dayOfWeek = :day AND s.classroom.id = :classroomId")
    List<ScheduleSlot> findByDayAndClassroom(DayOfWeek day, Long classroomId);
}
