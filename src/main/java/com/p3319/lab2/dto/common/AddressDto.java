package com.p3319.lab2.dto.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;

    @Size(min = 6, message = "Индекс должен содержать не менее 6 символов")
    private String zipCode;

    @Valid
    private LocationDto town;
}
