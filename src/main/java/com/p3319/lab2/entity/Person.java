package com.p3319.lab2.entity;

import com.p3319.lab2.entity.enums.Color;
import com.p3319.lab2.entity.enums.Country;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Person.name can't be blank")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "eye_color")
    private Color eyeColor; // Can be null

    @NotNull(message = "Person.hairColor can't be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color", nullable = false)
    private Color hairColor;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id", unique = true)
    private Location location; // Can be null

    @Positive(message = "Person.weight must be greater than 0")
    @Column(name = "weight")
    private Integer weight; // Can be null

    @NotNull(message = "Person.nationality can't be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Country nationality;
}
