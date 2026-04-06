package com.agriserve.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum Role {
        FARMER, OFFICER, MANAGER, ADMIN, COMPLIANCE, AUDITOR
    }

    public enum UserStatus{
        ACTIVE, INACTIVE, SUSPENDED
    }
}