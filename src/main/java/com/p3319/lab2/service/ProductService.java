package com.p3319.lab2.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.p3319.lab2.dto.event.EventType;
import com.p3319.lab2.dto.product.ProductRequestDto;
import com.p3319.lab2.dto.product.ProductResponseDto;
import com.p3319.lab2.dto.special.RatingGroupDto;
import com.p3319.lab2.entity.Organization;
import com.p3319.lab2.entity.Person;
import com.p3319.lab2.entity.Product;
import com.p3319.lab2.entity.enums.UnitOfMeasure;
import com.p3319.lab2.exception.ConflictException;
import com.p3319.lab2.exception.ResourceNotFoundException;
import com.p3319.lab2.mapper.EntityMapper;
import com.p3319.lab2.repository.OrganizationRepository;
import com.p3319.lab2.repository.PersonRepository;
import com.p3319.lab2.repository.ProductRepository;
import com.p3319.lab2.repository.ProductSpecifications;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final OrganizationRepository organizationRepository;
    private final PersonRepository personRepository;
    private final EntityMapper mapper;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAll(
            String name,
            String partNumber,
            UnitOfMeasure unitOfMeasure,
            String manufacturerName,
            String ownerName,
            Pageable pageable) {

        Specification<Product> spec = ProductSpecifications.filterBy(
                name, partNumber, unitOfMeasure, manufacturerName, ownerName
        );
        return productRepository.findAll(spec, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        return mapper.toDto(product);
    }

    @Transactional
    public ProductResponseDto create(ProductRequestDto dto) {
        return create(dto, true);
    }

    @Transactional
    public ProductResponseDto create(ProductRequestDto dto, boolean notify) {
        Organization manufacturer = organizationRepository.findById(dto.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization with id " + dto.getManufacturerId() + " not found"));

        Person owner = personRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + dto.getOwnerId() + " not found"));

        if (productRepository.countDuplicates(dto.getName(), dto.getManufacturerId(), 0) > 0) {
            throw new ConflictException("Продукт с таким названием у этого производителя уже существует");
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setCoordinates(mapper.toEntity(dto.getCoordinates()));
        product.setUnitOfMeasure(dto.getUnitOfMeasure());
        product.setManufacturer(manufacturer);
        product.setPrice(dto.getPrice());
        product.setManufactureCost(dto.getManufactureCost());
        product.setRating(dto.getRating());
        product.setPartNumber(dto.getPartNumber());
        product.setOwner(owner);

        Product saved = productRepository.save(product);
        ProductResponseDto responseDto = mapper.toDto(saved);
        if (notify) {
            notificationService.sendProductEvent(EventType.CREATED, saved.getId(), responseDto);
        }
        return responseDto;
    }

    @Transactional
    public ProductResponseDto update(Integer id, ProductRequestDto dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        Organization manufacturer = organizationRepository.findById(dto.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization with id " + dto.getManufacturerId() + " not found"));

        Person owner = personRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + dto.getOwnerId() + " not found"));

        if (productRepository.countDuplicates(dto.getName(), dto.getManufacturerId(), id) > 0) {
            throw new ConflictException("Продукт с таким названием у этого производителя уже существует");
        }

        existing.setName(dto.getName());
        if (existing.getCoordinates() != null && dto.getCoordinates() != null) {
            existing.getCoordinates().setX(dto.getCoordinates().getX());
            existing.getCoordinates().setY(dto.getCoordinates().getY());
        } else {
            existing.setCoordinates(mapper.toEntity(dto.getCoordinates()));
        }
        existing.setUnitOfMeasure(dto.getUnitOfMeasure());
        existing.setManufacturer(manufacturer);
        existing.setPrice(dto.getPrice());
        existing.setManufactureCost(dto.getManufactureCost());
        existing.setRating(dto.getRating());
        existing.setPartNumber(dto.getPartNumber());
        existing.setOwner(owner);

        Product saved = productRepository.save(existing);
        ProductResponseDto responseDto = mapper.toDto(saved);
        notificationService.sendProductEvent(EventType.UPDATED, saved.getId(), responseDto);
        return responseDto;
    }

    @Transactional
    public void delete(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
        notificationService.sendProductEvent(EventType.DELETED, id, null);
    }

    // special 1
    @Transactional(readOnly = true)
    public ProductResponseDto getMinPartNumber() {
        return productRepository.findAll().stream()
                .min(Comparator.comparing(Product::getPartNumber))
                .map(mapper::toDto)
                .orElse(null);
    }

    // special 2
    @Transactional(readOnly = true)
    public List<RatingGroupDto> groupByRating() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(Product::getRating, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new RatingGroupDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(RatingGroupDto::getRating).reversed())
                .toList();
    }

    // special 3
    @Transactional(readOnly = true)
    public long countByPartNumber(String partNumber) {
        if (partNumber == null || partNumber.isBlank()) {
            return 0L;
        }
        return productRepository.findAll().stream()
                .filter(p -> partNumber.trim().equals(p.getPartNumber()))
                .count();
    }

    // special 4
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getByManufacturer(Long manufacturerId, Pageable pageable) {
        if (!organizationRepository.existsById(manufacturerId)) {
            throw new ResourceNotFoundException("Organization with id " + manufacturerId + " not found");
        }
        return productRepository.findAllByManufacturerId(manufacturerId, pageable).map(mapper::toDto);
    }

    // special 5
    @Transactional
    public void reducePrice(Double percent) {
        if (percent == null || percent <= 0) {
            throw new IllegalArgumentException("Percent must be greater than 0");
        }

        List<Product> products = productRepository.findAll();
        float factor = 1.0f - (float) (percent / 100.0);

        for (Product p : products) {
            float newPrice = p.getPrice() * factor;
            if (newPrice <= 0.0f) {
                throw new IllegalArgumentException(
                        "Discount of " + percent + "% results in non-positive price (" + newPrice + ") for product id " + p.getId()
                );
            }
            p.setPrice(newPrice);
        }

        productRepository.saveAll(products);

        for (Product p : products) {
            notificationService.sendProductEvent(EventType.UPDATED, p.getId(), mapper.toDto(p));
        }
    }
}
