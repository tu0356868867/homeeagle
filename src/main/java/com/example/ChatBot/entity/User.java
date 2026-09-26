package com.example.ChatBot.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {@Id
@GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    String fullname;
    String password;
    String email;
    String feedback;

    @OneToMany(mappedBy = "user")
    private List<Manager> managers;

    @ManyToMany
    private Set<Role> roles;
}
