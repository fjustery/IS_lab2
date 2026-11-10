package com.p3319.lab2.mapper;

import org.springframework.stereotype.Component;

import com.p3319.lab2.dto.common.AddressDto;
import com.p3319.lab2.dto.common.CoordinatesDto;
import com.p3319.lab2.dto.common.LocationDto;
import com.p3319.lab2.dto.imports.ImportOperationResponseDto;
import com.p3319.lab2.dto.organization.OrganizationRequestDto;
import com.p3319.lab2.dto.organization.OrganizationResponseDto;
import com.p3319.lab2.dto.organization.OrganizationShortDto;
import com.p3319.lab2.dto.person.PersonRequestDto;
import com.p3319.lab2.dto.person.PersonResponseDto;
import com.p3319.lab2.dto.person.PersonShortDto;
import com.p3319.lab2.dto.product.ProductResponseDto;
import com.p3319.lab2.entity.Address;
import com.p3319.lab2.entity.Coordinates;
import com.p3319.lab2.entity.ImportOperation;
import com.p3319.lab2.entity.Location;
import com.p3319.lab2.entity.Organization;
import com.p3319.lab2.entity.Person;
import com.p3319.lab2.entity.Product;

@Component
public class EntityMapper {

    public LocationDto toDto(Location entity) {
        if (entity == null) return null;
        return new LocationDto(entity.getId(), entity.getX(), entity.getY(), entity.getName());
    }

    public Location toEntity(LocationDto dto) {
        if (dto == null) return null;
        Location location = new Location();
        location.setId(dto.getId());
        location.setX(dto.getX());
        location.setY(dto.getY());
        location.setName(dto.getName());
        return location;
    }

    public CoordinatesDto toDto(Coordinates entity) {
        if (entity == null) return null;
        return new CoordinatesDto(entity.getId(), entity.getX(), entity.getY());
    }

    public Coordinates toEntity(CoordinatesDto dto) {
        if (dto == null) return null;
        Coordinates coordinates = new Coordinates();
        coordinates.setId(dto.getId());
        coordinates.setX(dto.getX());
        coordinates.setY(dto.getY());
        return coordinates;
    }

    public AddressDto toDto(Address entity) {
        if (entity == null) return null;
        return new AddressDto(entity.getId(), entity.getZipCode(), toDto(entity.getTown()));
    }

    public Address toEntity(AddressDto dto) {
        if (dto == null) return null;
        Address address = new Address();
        address.setId(dto.getId());
        address.setZipCode(dto.getZipCode());
        address.setTown(toEntity(dto.getTown()));
        return address;
    }

    public OrganizationResponseDto toDto(Organization entity) {
        if (entity == null) return null;
        return new OrganizationResponseDto(
                entity.getId(),
                entity.getName(),
                toDto(entity.getOfficialAddress()),
                entity.getAnnualTurnover(),
                entity.getEmployeesCount(),
                entity.getRating(),
                entity.getType(),
                toDto(entity.getPostalAddress())
        );
    }

    public OrganizationShortDto toShortDto(Organization entity) {
        if (entity == null) return null;
        return new OrganizationShortDto(entity.getId(), entity.getName());
    }

    public Organization toEntity(OrganizationRequestDto dto) {
        if (dto == null) return null;
        Organization organization = new Organization();
        organization.setName(dto.getName());
        organization.setOfficialAddress(toEntity(dto.getOfficialAddress()));
        organization.setAnnualTurnover(dto.getAnnualTurnover());
        organization.setEmployeesCount(dto.getEmployeesCount());
        organization.setRating(dto.getRating());
        organization.setType(dto.getType());
        organization.setPostalAddress(toEntity(dto.getPostalAddress()));
        return organization;
    }

    public PersonResponseDto toDto(Person entity) {
        if (entity == null) return null;
        return new PersonResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getEyeColor(),
                entity.getHairColor(),
                toDto(entity.getLocation()),
                entity.getWeight(),
                entity.getNationality()
        );
    }

    public PersonShortDto toShortDto(Person entity) {
        if (entity == null) return null;
        return new PersonShortDto(entity.getId(), entity.getName());
    }

    public Person toEntity(PersonRequestDto dto) {
        if (dto == null) return null;
        Person person = new Person();
        person.setName(dto.getName());
        person.setEyeColor(dto.getEyeColor());
        person.setHairColor(dto.getHairColor());
        person.setLocation(toEntity(dto.getLocation()));
        person.setWeight(dto.getWeight());
        person.setNationality(dto.getNationality());
        return person;
    }

    public ProductResponseDto toDto(Product entity) {
        if (entity == null) return null;
        return new ProductResponseDto(
                entity.getId(),
                entity.getName(),
                toDto(entity.getCoordinates()),
                entity.getCreationDate(),
                entity.getUnitOfMeasure(),
                toDto(entity.getManufacturer()),
                entity.getPrice(),
                entity.getManufactureCost(),
                entity.getRating(),
                entity.getPartNumber(),
                toDto(entity.getOwner())
        );
    }

    public ImportOperationResponseDto toDto(ImportOperation entity) {
        if (entity == null) return null;
        return new ImportOperationResponseDto(
            entity.getId(),
            entity.getStatus(),
            entity.getUser().getUsername(),
            entity.getAddedCount()
        );
    }
}
