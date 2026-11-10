package com.p3319.lab2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coordinates")
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = -219, message = "Coordinates.x must be greater than -220")
    @Column(nullable = false)
    private long x;

    @NotNull(message = "Coordinates.y can't be null")
    @Column(nullable = false)
    private Double y;
}
