package com.p3319.lab2.dto.organization;

import com.p3319.lab2.dto.common.AddressDto;
import com.p3319.lab2.entity.enums.OrganizationType;

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
public class OrganizationRequestDto {

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @NotNull(message = "Официальный адрес обязателен")
    @Valid
    private AddressDto officialAddress;

    @NotNull(message = "Годовой оборот обязателен")
    @Positive(message = "Годовой оборот должен быть больше 0")
    private Long annualTurnover;

    @NotNull(message = "Количество сотрудников обязательно")
    @Positive(message = "Количество сотрудников должно быть больше 0")
    private Integer employeesCount;

    @NotNull(message = "Рейтинг обязателен")
    @Positive(message = "Рейтинг должен быть больше 0")
    private Float rating;

    private OrganizationType type; // Can be null

    @NotNull(message = "Почтовый адрес обязателен")
    @Valid
    private AddressDto postalAddress;
}
