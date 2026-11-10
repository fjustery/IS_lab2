package com.p3319.lab2.dto.special;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingGroupDto {
    private Double rating;
    private Long count;
}
