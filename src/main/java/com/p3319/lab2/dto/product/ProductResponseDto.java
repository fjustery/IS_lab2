package com.p3319.lab2.dto.product;

import java.time.ZonedDateTime;

import com.p3319.lab2.dto.common.CoordinatesDto;
import com.p3319.lab2.dto.organization.OrganizationResponseDto;
import com.p3319.lab2.dto.person.PersonResponseDto;
import com.p3319.lab2.entity.enums.UnitOfMeasure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Integer id;
    private String name;
    private CoordinatesDto coordinates;
    private ZonedDateTime creationDate;
    private UnitOfMeasure unitOfMeasure;
    private OrganizationResponseDto manufacturer;
    private float price;
    private Double manufactureCost;
    private double rating;
    private String partNumber;
    private PersonResponseDto owner;
}
