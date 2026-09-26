package com.example.ChatBot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "Roomate")
public class Roomate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String address;

    @NotBlank
    @Column(nullable = false)
    private Integer price;

    @NotBlank
    @Column(nullable = false)
    private String type;

    @NotBlank
    @Column(nullable = false)
    private String picture;

    @NotBlank
    @Column(nullable = false)
    private String contact;

    @NotBlank
    @Column(nullable = false)
    private Integer max_roomate;

    @NotBlank
    @Column(nullable = false)
    private Integer area;
    private String infor;

    @OneToOne
    @JoinColumn(name = "user_id",
            unique = true)
    private User user;
}
