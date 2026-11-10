package com.p3319.lab2.dto.organization;

import com.p3319.lab2.dto.common.AddressDto;
import com.p3319.lab2.entity.enums.OrganizationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationResponseDto {
    private Long id;
    private String name;
    private AddressDto officialAddress;
    private Long annualTurnover;
    private int employeesCount;
    private Float rating;
    private OrganizationType type;
    private AddressDto postalAddress;
}
