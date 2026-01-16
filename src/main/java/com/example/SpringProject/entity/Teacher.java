package com.example.SpringProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends User {

    @Column(unique = true, nullable = false)
    private String teacherId;

    @Column(nullable = false)
    private String specialization;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    public Teacher(String username, String password, String email, String firstName,
            String lastName, String teacherId, String specialization) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = UserRole.TEACHER;
        this.teacherId = teacherId;
        this.specialization = specialization;
    }
}
