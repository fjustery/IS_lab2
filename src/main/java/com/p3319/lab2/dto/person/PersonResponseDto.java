package com.p3319.lab2.dto.person;

import com.p3319.lab2.dto.common.LocationDto;
import com.p3319.lab2.entity.enums.Color;
import com.p3319.lab2.entity.enums.Country;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponseDto {
    private Long id;
    private String name;
    private Color eyeColor;
    private Color hairColor;
    private LocationDto location;
    private Integer weight;
    private Country nationality;
}
