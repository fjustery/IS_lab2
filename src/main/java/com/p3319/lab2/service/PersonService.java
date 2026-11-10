package com.p3319.lab2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.p3319.lab2.dto.event.EventType;
import com.p3319.lab2.dto.person.PersonRequestDto;
import com.p3319.lab2.dto.person.PersonResponseDto;
import com.p3319.lab2.entity.Person;
import com.p3319.lab2.exception.ConflictException;
import com.p3319.lab2.exception.ResourceNotFoundException;
import com.p3319.lab2.mapper.EntityMapper;
import com.p3319.lab2.repository.PersonRepository;
import com.p3319.lab2.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final ProductRepository productRepository;
    private final EntityMapper mapper;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public Page<PersonResponseDto> getAll(Pageable pageable) {
        return personRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public PersonResponseDto getById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));
        return mapper.toDto(person);
    }

    @Transactional
    public PersonResponseDto create(PersonRequestDto dto) {
        return create(dto, true);
    }

    @Transactional
    public PersonResponseDto create(PersonRequestDto dto, boolean notify) {
        Person person = mapper.toEntity(dto);
        Person saved = personRepository.save(person);
        PersonResponseDto responseDto = mapper.toDto(saved);
        if (notify) {
            notificationService.sendPersonEvent(EventType.CREATED, saved.getId(), responseDto);
        }
        return responseDto;
    }

    @Transactional
    public PersonResponseDto update(Long id, PersonRequestDto dto) {
        Person existing = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));

        existing.setName(dto.getName());
        existing.setEyeColor(dto.getEyeColor());
        existing.setHairColor(dto.getHairColor());
        if (dto.getLocation() == null) {
            existing.setLocation(null);
        } else if (existing.getLocation() != null) {
            existing.getLocation().setX(dto.getLocation().getX());
            existing.getLocation().setY(dto.getLocation().getY());
            existing.getLocation().setName(dto.getLocation().getName());
        } else {
            existing.setLocation(mapper.toEntity(dto.getLocation()));
        }
        existing.setWeight(dto.getWeight());
        existing.setNationality(dto.getNationality());

        Person saved = personRepository.save(existing);
        PersonResponseDto responseDto = mapper.toDto(saved);
        notificationService.sendPersonEvent(EventType.UPDATED, saved.getId(), responseDto);
        return responseDto;
    }

    @Transactional
    public void delete(Long id) {
        if (!personRepository.existsById(id)) {
            throw new ResourceNotFoundException("Person with id " + id + " not found");
        }
        if (productRepository.existsByOwnerId(id)) {
            throw new ConflictException("Cannot delete person: referenced by products");
        }

        personRepository.deleteById(id);
        notificationService.sendPersonEvent(EventType.DELETED, id, null);
    }
}
