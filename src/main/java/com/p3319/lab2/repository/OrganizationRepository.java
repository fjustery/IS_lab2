package com.p3319.lab2.repository;

import com.p3319.lab2.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Page<Organization> findAll(Pageable pageable);

    @Query("select count(o) from Organization o where lower(trim(o.name)) = lower(trim(:name)) and o.id <> :excludeId")
    long countDuplicates(@Param("name") String name, @Param("excludeId") Long excludeId);
}
