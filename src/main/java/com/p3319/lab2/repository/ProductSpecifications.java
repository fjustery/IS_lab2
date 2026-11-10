package com.p3319.lab2.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.p3319.lab2.entity.Product;
import com.p3319.lab2.entity.enums.UnitOfMeasure;

import jakarta.persistence.criteria.Predicate;

public final class ProductSpecifications {

    private ProductSpecifications() { }

    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("name"), name.trim());
        };
    }

    public static Specification<Product> hasPartNumber(String partNumber) {
        return (root, query, cb) -> {
            if (partNumber == null || partNumber.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("partNumber"), partNumber.trim());
        };
    }

    public static Specification<Product> hasUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        return (root, query, cb) -> {
            if (unitOfMeasure == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("unitOfMeasure"), unitOfMeasure);
        };
    }

    public static Specification<Product> hasManufacturerName(String manufacturerName) {
        return (root, query, cb) -> {
            if (manufacturerName == null || manufacturerName.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.join("manufacturer").get("name"), manufacturerName.trim());
        };
    }

    public static Specification<Product> hasOwnerName(String ownerName) {
        return (root, query, cb) -> {
            if (ownerName == null || ownerName.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.join("owner").get("name"), ownerName.trim());
        };
    }

    public static Specification<Product> filterBy(
            String name,
            String partNumber,
            UnitOfMeasure unitOfMeasure,
            String manufacturerName,
            String ownerName) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(cb.equal(root.get("name"), name.trim()));
            }
            if (partNumber != null && !partNumber.isBlank()) {
                predicates.add(cb.equal(root.get("partNumber"), partNumber.trim()));
            }
            if (unitOfMeasure != null) {
                predicates.add(cb.equal(root.get("unitOfMeasure"), unitOfMeasure));
            }
            if (manufacturerName != null && !manufacturerName.isBlank()) {
                predicates.add(cb.equal(root.join("manufacturer").get("name"), manufacturerName.trim()));
            }
            if (ownerName != null && !ownerName.isBlank()) {
                predicates.add(cb.equal(root.join("owner").get("name"), ownerName.trim()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
