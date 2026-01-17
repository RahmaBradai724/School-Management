package com.example.SpringProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "courses")
@Data
@EqualsAndHashCode(exclude = { "enrollments", "studentGroups" })
@ToString(exclude = { "enrollments", "studentGroups" })
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String courseCode;

    @Column(nullable = false)
    private String courseName;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int credits;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    @JsonIgnoreProperties({ "courses", "password" })
    private Teacher teacher;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("course")
    private List<Enrollment> enrollments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;

    @ManyToMany
    @JoinTable(name = "course_groups", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<StudentGroup> studentGroups = new ArrayList<>();
}
