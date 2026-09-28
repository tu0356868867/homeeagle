package com.example.ChatBot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "home")
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String address;

    @NotBlank
    @Column(nullable = false)
    private String  contact;

    @NotBlank
    @Column(nullable = false)
    private Integer  price;

    @NotBlank
    @Column(nullable = false)
    private Integer quantity;
    private String type;

    @NotBlank
    @Column(nullable = false)
    private Integer area;

    @NotBlank
    @Column(nullable = false)
    private String picture;

    @Column(name = "descr")
    private String describe;
    private boolean statusDelete = false;

    @OneToMany(mappedBy = "home")
    private List<Manager> managers;
}
