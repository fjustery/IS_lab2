package com.p3319.lab2.service;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.p3319.lab2.dto.imports.ImportOperationResponseDto;
import com.p3319.lab2.dto.organization.OrganizationRequestDto;
import com.p3319.lab2.dto.person.PersonRequestDto;
import com.p3319.lab2.dto.product.ProductRequestDto;
import com.p3319.lab2.entity.User;
import com.p3319.lab2.exception.ConflictException;
import com.p3319.lab2.exception.ImportRejectedException;
import com.p3319.lab2.exception.ResourceNotFoundException;
import com.p3319.lab2.imports.ImportFile;
import com.p3319.lab2.imports.ImportFileParser;
import com.p3319.lab2.imports.ImportItemReader;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportFileParser parser;
    private final ImportItemReader reader;
    private final ProductService productService;
    private final OrganizationService organizationService;
    private final PersonService personService;
    private final ImportJournalService journalService;
    private final TransactionTemplate transactionTemplate;

    public ImportOperationResponseDto importFile(MultipartFile file, User user) {
        ImportFile importFile = parser.parse(file);

        try {
            return transactionTemplate.execute(status -> execute(importFile, user));
        } catch (RuntimeException e) {
            journalService.recordFailure(user);
            throw e;
        }
    }

    private ImportOperationResponseDto execute(ImportFile importFile, User user) {
        int created = switch (importFile.resourceType()) {
            case PRODUCTS -> importAll(importFile.items(), ProductRequestDto.class,
                    dto -> productService.create(dto, false));
            case ORGANIZATIONS -> importAll(importFile.items(), OrganizationRequestDto.class,
                    dto -> organizationService.create(dto, false));
            case PERSONS -> importAll(importFile.items(), PersonRequestDto.class,
                    dto -> personService.create(dto, false));
        };
        return journalService.recordSuccess(user, created);
    }

    private <T> int importAll(List<JsonNode> items, Class<T> type, Consumer<T> creator) {
        for (int position = 0; position < items.size(); position++) {
            int index = position + 1;
            T dto = reader.read(items.get(position), type, index);
            try {
                creator.accept(dto);
            } catch (ResourceNotFoundException e) {
                throw new ImportRejectedException("Запись " + index + ": " + e.getMessage());
            } catch (ConflictException e) {
                throw new ConflictException("Запись " + index + ": " + e.getMessage());
            }
        }
        return items.size();
    }
}
