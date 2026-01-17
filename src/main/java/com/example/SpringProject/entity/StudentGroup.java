package com.example.SpringProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g., "Group TP1", "Class A"

    @ManyToOne
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;

    @ManyToOne
    @JoinColumn(name = "academic_session_id")
    private AcademicSession academicSession;
}
