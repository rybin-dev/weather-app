package com.rybindev.weatherapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users", indexes = {@Index(name = "user_email_idx", columnList = "email")})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "id")
public class User implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Location> locations = new HashSet<>();
}
