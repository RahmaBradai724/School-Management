package com.example.SpringProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "grades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "enrollment_id", nullable = false)
    @JsonIgnoreProperties("grades")
    private Enrollment enrollment;

    @Column(nullable = false)
    private String examType; // e.g., "Midterm", "Final", "Quiz", "Project"

    @Column(nullable = false)
    private double score;

    @Column(nullable = false)
    private LocalDate dateRecorded;

    @Column(length = 500)
    private String comments;
}
