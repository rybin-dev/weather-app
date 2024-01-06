package com.rybindev.weatherapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Table(indexes = {@Index(name = "latitude_longitude_idx", columnList = "latitude, longitude", unique = true)})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Location implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Float latitude;

    private Float longitude;

    private Float temperature;

    private String country;

    @Builder.Default
    private Instant updatedAt = Instant.now();

    public boolean isExpired() {
        return Instant.now().minus(1, ChronoUnit.HOURS).isAfter(this.getUpdatedAt());
    }

}
