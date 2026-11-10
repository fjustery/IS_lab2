package com.p3319.lab2.dto.special;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReducePriceRequest {
    @NotNull(message = "ReducePrice.percent can't be null")
    @Positive(message = "ReducePrice.percent must be greater than 0")
    private Double percent;
}
