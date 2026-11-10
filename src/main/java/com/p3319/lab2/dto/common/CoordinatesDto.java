package com.p3319.lab2.dto.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesDto {
    private Long id;

    @NotNull(message = "Координата X обязательна")
    @Min(value = -219, message = "Координата X должна быть больше -220")
    private Long x;

    @NotNull(message = "Координата Y обязательна")
    private Double y;
}
