package com.p3319.lab2.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.p3319.lab2.dto.common.PageResponse;
import com.p3319.lab2.dto.imports.ImportOperationResponseDto;
import com.p3319.lab2.entity.User;
import com.p3319.lab2.service.ImportJournalService;
import com.p3319.lab2.service.ImportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;
    private final ImportJournalService journalService;

    @GetMapping
    public ResponseEntity<PageResponse<ImportOperationResponseDto>> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(PageResponse.of(journalService.getAll(user, pageable)));
    }

    @PostMapping
    public ResponseEntity<ImportOperationResponseDto> importFile(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(importService.importFile(file, user));
    }
}
