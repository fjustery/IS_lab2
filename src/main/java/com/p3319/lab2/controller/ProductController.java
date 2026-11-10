package com.p3319.lab2.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.p3319.lab2.dto.common.PageResponse;
import com.p3319.lab2.dto.product.ProductRequestDto;
import com.p3319.lab2.dto.product.ProductResponseDto;
import com.p3319.lab2.entity.enums.UnitOfMeasure;
import com.p3319.lab2.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponseDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String partNumber,
            @RequestParam(required = false) UnitOfMeasure unitOfMeasure,
            @RequestParam(required = false) String manufacturerName,
            @RequestParam(required = false) String ownerName,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok(PageResponse.of(
                productService.getAll(name, partNumber, unitOfMeasure, manufacturerName, ownerName, pageable)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
