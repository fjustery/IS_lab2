package com.p3319.lab2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.p3319.lab2.dto.imports.ImportOperationResponseDto;
import com.p3319.lab2.entity.ImportOperation;
import com.p3319.lab2.entity.User;
import com.p3319.lab2.entity.enums.ImportStatus;
import com.p3319.lab2.entity.enums.Role;
import com.p3319.lab2.mapper.EntityMapper;
import com.p3319.lab2.repository.ImportOperationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ImportJournalService {

  private static final int MAX_PAGE_SIZE = 100;

  private final ImportOperationRepository importOperationRepository;
  private final EntityMapper mapper;

  @Transactional 
  public ImportOperationResponseDto recordSuccess(User user, int addedCount) {
    ImportOperation importOperation = new ImportOperation();
    importOperation.setUser(user);
    importOperation.setAddedCount(addedCount);
    importOperation.setStatus(ImportStatus.SUCCESS);
    return mapper.toDto(importOperationRepository.save(importOperation));
  }

  @Transactional 
  public void recordFailure(User user) {
    ImportOperation importOperation = new ImportOperation();
    importOperation.setUser(user);
    importOperation.setAddedCount(null);
    importOperation.setStatus(ImportStatus.FAILED);
    importOperationRepository.save(importOperation);
  }

  @Transactional(readOnly = true)
  public Page<ImportOperationResponseDto> getAll(User user, Pageable pageable) {
    Pageable page = PageRequest.of(pageable.getPageNumber(), Math.min(pageable.getPageSize(), MAX_PAGE_SIZE));
    Page<ImportOperation> operations = user.getRole() == Role.ADMIN
        ? importOperationRepository.findAllByOrderByIdDesc(page)
        : importOperationRepository.findAllByUserIdOrderByIdDesc(user.getId(), page);
    return operations.map(mapper::toDto);
  }
}
