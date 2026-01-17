package com.example.SpringProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@EqualsAndHashCode(callSuper = true, exclude = { "enrollments", "studentGroup" })
@ToString(callSuper = true, exclude = { "enrollments", "studentGroup" })
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = UserRole.STUDENT;
        }
    }

    @Column(unique = true, nullable = false)
    private String studentId;

    @Column(nullable = false)
    private LocalDate enrollmentDate;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "group_id")
    private StudentGroup studentGroup;

    public Student(String username, String password, String email, String firstName,
            String lastName, String studentId, LocalDate enrollmentDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = UserRole.STUDENT;
        this.studentId = studentId;
        this.enrollmentDate = enrollmentDate;
    }
}
