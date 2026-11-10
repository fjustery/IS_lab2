package com.p3319.lab2.dto.common;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private Long id;
    @NotNull(message = "Координата X обязательна")
    private Long x;

    @NotNull(message = "Координата Y обязательна")
    private Long y;

    private String name;
}
