package com.p3319.lab2.dto.imports;

import com.p3319.lab2.entity.enums.ImportStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportOperationResponseDto {
    private Long id;
    private ImportStatus status;
    private String username;
    private Integer addedCount;
}
