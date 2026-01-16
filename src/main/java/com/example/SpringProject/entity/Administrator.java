package com.example.SpringProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "administrators")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Administrator extends User {

    @Column(unique = true, nullable = false)
    private String adminId;

    @Column(nullable = false)
    private String department;

    public Administrator(String username, String password, String email, String firstName,
            String lastName, String adminId, String department) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = UserRole.ADMIN;
        this.adminId = adminId;
        this.department = department;
    }
}
