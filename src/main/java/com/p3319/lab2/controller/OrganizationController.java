package com.p3319.lab2.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.p3319.lab2.dto.common.PageResponse;
import com.p3319.lab2.dto.organization.OrganizationRequestDto;
import com.p3319.lab2.dto.organization.OrganizationResponseDto;
import com.p3319.lab2.service.OrganizationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<PageResponse<OrganizationResponseDto>> getAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.of(organizationService.getAll(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.getById(id));
    }

    @PostMapping
    public ResponseEntity<OrganizationResponseDto> create(@Valid @RequestBody OrganizationRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationRequestDto dto) {
        return ResponseEntity.ok(organizationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
