package com.p3319.lab2.dto.person;

import com.p3319.lab2.dto.common.LocationDto;
import com.p3319.lab2.entity.enums.Color;
import com.p3319.lab2.entity.enums.Country;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequestDto {

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    private Color eyeColor; // Can be null

    @NotNull(message = "Цвет волос обязателен")
    private Color hairColor;

    @Valid
    private LocationDto location; // Can be null

    @Positive(message = "Вес должен быть больше 0")
    private Integer weight; // Can be null

    @NotNull(message = "Национальность обязательна")
    private Country nationality;
}
