package com.p3319.lab2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.p3319.lab2.dto.common.AddressDto;
import com.p3319.lab2.dto.event.EventType;
import com.p3319.lab2.dto.organization.OrganizationRequestDto;
import com.p3319.lab2.dto.organization.OrganizationResponseDto;
import com.p3319.lab2.entity.Address;
import com.p3319.lab2.entity.Organization;
import com.p3319.lab2.exception.ConflictException;
import com.p3319.lab2.exception.ResourceNotFoundException;
import com.p3319.lab2.mapper.EntityMapper;
import com.p3319.lab2.repository.OrganizationRepository;
import com.p3319.lab2.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final ProductRepository productRepository;
    private final EntityMapper mapper;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public Page<OrganizationResponseDto> getAll(Pageable pageable) {
        return organizationRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public OrganizationResponseDto getById(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization with id " + id + " not found"));
        return mapper.toDto(organization);
    }

    @Transactional
    public OrganizationResponseDto create(OrganizationRequestDto dto) {
        return create(dto, true);
    }

    @Transactional
    public OrganizationResponseDto create(OrganizationRequestDto dto, boolean notify) {
        if (organizationRepository.countDuplicates(dto.getName(), 0L) > 0) {
            throw new ConflictException("Организация с таким названием уже существует");
        }

        Organization organization = mapper.toEntity(dto);
        Organization saved = organizationRepository.save(organization);
        OrganizationResponseDto responseDto = mapper.toDto(saved);
        if (notify) {
            notificationService.sendOrganizationEvent(EventType.CREATED, saved.getId(), responseDto);
        }
        return responseDto;
    }

    @Transactional
    public OrganizationResponseDto update(Long id, OrganizationRequestDto dto) {
        Organization existing = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization with id " + id + " not found"));

        if (organizationRepository.countDuplicates(dto.getName(), id) > 0) {
            throw new ConflictException("Организация с таким названием уже существует");
        }

        existing.setName(dto.getName());
        existing.setAnnualTurnover(dto.getAnnualTurnover());
        existing.setEmployeesCount(dto.getEmployeesCount());
        existing.setRating(dto.getRating());
        existing.setType(dto.getType());

        updateAddress(existing.getOfficialAddress(), dto.getOfficialAddress());
        updateAddress(existing.getPostalAddress(), dto.getPostalAddress());

        Organization saved = organizationRepository.save(existing);
        OrganizationResponseDto responseDto = mapper.toDto(saved);
        notificationService.sendOrganizationEvent(EventType.UPDATED, saved.getId(), responseDto);
        return responseDto;
    }

    private void updateAddress(Address existingAddress, AddressDto dto) {
        if (existingAddress != null && dto != null) {
            existingAddress.setZipCode(dto.getZipCode());
            if (dto.getTown() == null) {
                existingAddress.setTown(null);
            } else if (existingAddress.getTown() != null) {
                existingAddress.getTown().setX(dto.getTown().getX());
                existingAddress.getTown().setY(dto.getTown().getY());
                existingAddress.getTown().setName(dto.getTown().getName());
            } else {
                existingAddress.setTown(mapper.toEntity(dto.getTown()));
            }
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!organizationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Organization with id " + id + " not found");
        }
        if (productRepository.existsByManufacturerId(id)) {
            throw new ConflictException("Cannot delete organization: referenced by products");
        }

        organizationRepository.deleteById(id);
        notificationService.sendOrganizationEvent(EventType.DELETED, id, null);
    }
}
