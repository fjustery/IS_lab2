package com.p3319.lab2.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.p3319.lab2.dto.common.PageResponse;
import com.p3319.lab2.dto.person.PersonRequestDto;
import com.p3319.lab2.dto.person.PersonResponseDto;
import com.p3319.lab2.service.PersonService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public ResponseEntity<PageResponse<PersonResponseDto>> getAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.of(personService.getAll(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PersonResponseDto> create(@Valid @RequestBody PersonRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody PersonRequestDto dto) {
        return ResponseEntity.ok(personService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
