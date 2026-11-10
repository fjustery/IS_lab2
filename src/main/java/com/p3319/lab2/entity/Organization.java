package com.p3319.lab2.entity;

import com.p3319.lab2.entity.enums.OrganizationType;

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
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Organization.name can't be blank")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Organization.officialAddress can't be null")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "official_address_id", nullable = false, unique = true)
    private Address officialAddress;

    @NotNull(message = "Organization.annualTurnover can't be null")
    @Positive(message = "Organization.annualTurnover must be greater than 0")
    @Column(name = "annual_turnover", nullable = false)
    private Long annualTurnover;

    @Positive(message = "Organization.employeesCount must be greater than 0")
    @Column(name = "employees_count", nullable = false)
    private int employeesCount;

    @NotNull(message = "Organization.rating can't be null")
    @Positive(message = "Organization.rating must be greater than 0")
    @Column(nullable = false)
    private Float rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OrganizationType type; // По ТЗ может быть null

    @NotNull(message = "Organization.postalAddress can't be null")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "postal_address_id", nullable = false, unique = true)
    private Address postalAddress;
}

