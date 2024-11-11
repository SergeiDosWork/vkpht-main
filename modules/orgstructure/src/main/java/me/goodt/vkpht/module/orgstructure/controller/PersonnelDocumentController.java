package me.goodt.vkpht.module.orgstructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.dto.OperationResult;
import me.goodt.vkpht.module.orgstructure.api.dto.PersonnelDocumentDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.PersonnelDocumentFactory;
import me.goodt.vkpht.common.api.ILoggerService;
import me.goodt.vkpht.module.orgstructure.api.IPersonnelDocumentService;

@RestController
@RequiredArgsConstructor
@SurProtected(operation = SurOperation.UNIT)
public class PersonnelDocumentController {

    private final IPersonnelDocumentService personnelDocumentService;
    private final ILoggerService loggerService;

    @Operation(summary = "Получение информации о кадровом документе", description = "Получение информации о кадровом документе", tags = {"personnel_document"})
    @BadRequestAPIResponses
    @GetMapping("/api/personneldocument/{id}")
    public PersonnelDocumentDto get(
        @Parameter(name = "id", description = "Идентификатор кадрового документа (таблица personnel_document)")
        @PathVariable("id") Long id) throws NotFoundException {
        return PersonnelDocumentFactory.create(personnelDocumentService.get(id));
    }

    @Operation(summary = "Получение информации о всех актуальных кадровых документах",
        description = "Получение информации о всех актуальных кадровых документах",
        tags = {"personnel_document"})
    @GetMapping("/api/personneldocument")
    public List<PersonnelDocumentDto> getAllActual() {
        return personnelDocumentService.getAllActual().stream()
            .map(PersonnelDocumentFactory::create)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Создание кадрового документа", description = "Создание кадрового документа", tags = {"personnel_document"})
    @PostMapping("/api/personneldocument")
    public PersonnelDocumentDto create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление создаваемого объекта")
        @RequestBody PersonnelDocumentDto dto) throws NotFoundException, JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/personneldocument", null, dto);
        return PersonnelDocumentFactory.create(personnelDocumentService.create(dto));
    }

    @Operation(summary = "Обновление кадрового документа", description = "Обновление кадрового документа", tags = {"personnel_document"})
    @BadRequestAPIResponses
    @PutMapping("/api/personneldocument/{id}")
    public PersonnelDocumentDto update(
        @Parameter(name = "id", description = "Идентификатор кадрового документа (таблица personnel_document)")
        @PathVariable("id") Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление обновляемого объекта")
        @RequestBody PersonnelDocumentDto dto) throws NotFoundException, JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/personneldocument/%d", id), null, dto);
        return PersonnelDocumentFactory.create(personnelDocumentService.update(id, dto));
    }

    @Operation(summary = "Удаление кадрового документа", description = "Удаление кадрового документа", tags = {"personnel_document"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/personneldocument/{id}")
    public OperationResult delete(
        @Parameter(name = "id", description = "Идентификатор кадрового документа (таблица personnel_document)")
        @PathVariable("id") Long id) throws NotFoundException, JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/personneldocument/%d", id), null, null);
        try {
            personnelDocumentService.delete(id);
            return new OperationResult(true, "");
        } catch (RuntimeException ex) {
            return new OperationResult(false, ex.getMessage());
        }
    }
}
