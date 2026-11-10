package com.p3319.lab2.entity;

import java.time.ZonedDateTime;

import com.p3319.lab2.entity.enums.UnitOfMeasure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Product.name can't be blank")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Product.coordinates can't be null")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "coordinates_id", nullable = false, unique = true)
    private Coordinates coordinates;

    @NotNull(message = "Product.creationDate can't be null")
    @Column(name = "creation_date", nullable = false, updatable = false)
    private ZonedDateTime creationDate = ZonedDateTime.now();

    @NotNull(message = "Product.unitOfMeasure can't be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measure", nullable = false)
    private UnitOfMeasure unitOfMeasure;

    @NotNull(message = "Product.manufacturer can't be null")
    @ManyToOne(optional = false)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Organization manufacturer;

    @Positive(message = "Product.price must be greater than 0")
    @Column(nullable = false)
    private float price;

    @Column(name = "manufacture_cost")
    private Double manufactureCost; // Can be null

    @Positive(message = "Product.rating must be greater than 0")
    @Column(nullable = false)
    private double rating;

    @NotBlank(message = "Product.partNumber can't be blank")
    @Size(min = 28, max = 84, message = "Product.partNumber length must be between 28 and 84")
    @Column(name = "part_number", nullable = false, length = 84)
    private String partNumber;

    @NotNull(message = "Product.owner can't be null")
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Person owner;

    @PrePersist
    protected void onCreate() {
        if (this.creationDate == null) {
            this.creationDate = ZonedDateTime.now();
        }
    }
}
