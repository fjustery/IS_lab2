package com.p3319.lab2.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.p3319.lab2.dto.common.PageResponse;
import com.p3319.lab2.dto.product.ProductResponseDto;
import com.p3319.lab2.dto.special.CountResponseDto;
import com.p3319.lab2.dto.special.RatingGroupDto;
import com.p3319.lab2.dto.special.ReducePriceRequest;
import com.p3319.lab2.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products/special")
@RequiredArgsConstructor
public class SpecialOperationsController {

    private final ProductService productService;

    @GetMapping("/min-part-number")
    public ResponseEntity<ProductResponseDto> getMinPartNumber() {
        return ResponseEntity.ok(productService.getMinPartNumber());
    }

    @GetMapping("/group-by-rating")
    public ResponseEntity<List<RatingGroupDto>> groupByRating() {
        return ResponseEntity.ok(productService.groupByRating());
    }

    @GetMapping("/count-by-part-number")
    public ResponseEntity<CountResponseDto> countByPartNumber(@RequestParam(required = false) String partNumber) {
        long count = productService.countByPartNumber(partNumber);
        return ResponseEntity.ok(new CountResponseDto(count));
    }

    @GetMapping("/by-manufacturer/{manufacturerId}")
    public ResponseEntity<PageResponse<ProductResponseDto>> getByManufacturer(
            @PathVariable Long manufacturerId,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.of(productService.getByManufacturer(manufacturerId, pageable)));
    }

    @PostMapping("/reduce-price")
    public ResponseEntity<Map<String, String>> reducePrice(@Valid @RequestBody ReducePriceRequest request) {
        productService.reducePrice(request.getPercent());
        return ResponseEntity.ok(Map.of("message", "Prices reduced by " + request.getPercent() + "% for all products"));
    }
}
