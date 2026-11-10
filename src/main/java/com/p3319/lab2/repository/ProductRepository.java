package com.p3319.lab2.repository;

import com.p3319.lab2.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    Page<Product> findAllByManufacturerId(Long manufacturerId, Pageable pageable);
    boolean existsByManufacturerId(Long manufacturerId);
    boolean existsByOwnerId(Long ownerId);

    @Query("select count(p) from Product p where lower(trim(p.name)) = lower(trim(:name)) and p.manufacturer.id = :manufacturerId and p.id <> :excludeId")
    long countDuplicates(@Param("name") String name, @Param("manufacturerId") Long manufacturerId, @Param("excludeId") Integer excludeId);
}
