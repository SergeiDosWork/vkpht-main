package me.goodt.vkpht.module.orgstructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import com.goodt.drive.rtcore.data.OperationResult;
import me.goodt.vkpht.module.orgstructure.api.dto.*;
import me.goodt.vkpht.common.application.exception.BadRequestException;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.ImportanceCriteriaFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.ImportanceCriteriaGroupFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.ImportanceCriteriaGroupTypeFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionImportanceCriteriaFactory;
import me.goodt.vkpht.module.orgstructure.domain.entity.*;
import com.goodt.drive.rtcore.security.AuthService;
import com.goodt.drive.rtcore.service.logging.ILoggerService;
import me.goodt.vkpht.module.orgstructure.api.IImportanceCriteriaService;
import me.goodt.vkpht.module.orgstructure.api.ILegalEntityTeamAssignmentService;
import me.goodt.vkpht.common.application.util.GlobalDefs;
import me.goodt.vkpht.common.application.util.TextConstants;

@RestController
@GeneralAPIResponses
@RequiredArgsConstructor
public class ImportanceCriteriaController {

    private final ILegalEntityTeamAssignmentService legalEntityTeamAssignmentService;
    private final IImportanceCriteriaService importanceCriteriaService;
    private final ILoggerService loggerService;
    private final AuthService authService;

    @Operation(summary = "Получение информации о всех типах групп критериев важности", description = "Получение информации о всех типах групп критериев важности", tags = {"importancecriteria"})
    @BadRequestAPIResponses
    @GetMapping("/api/importancecriteriagrouptype/list")
    public List<ImportanceCriteriaGroupTypeDto> findImportanceCriteriaGroupTypeAll() throws AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        List<ImportanceCriteriaGroupTypeEntity> entities = importanceCriteriaService.findImportanceCriteriaGroupTypeAll();
        return entities.stream()
            .map(ImportanceCriteriaGroupTypeFactory::create)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Создание типа группы критериев важности", description = "Создание типа группы критериев важности", tags = {"importancecriteria"})
    @BadRequestAPIResponses
    @PostMapping("/api/importancecriteriagrouptype/create")
    public ImportanceCriteriaGroupTypeDto createImportanceCriteriaGroupType(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица importance_criteria_group_type)")
        @RequestBody ImportanceCriteriaGroupTypeDto dto) throws JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/importancecriteriagrouptype/create", null, dto);
        ImportanceCriteriaGroupTypeEntity entity = importanceCriteriaService.createImportanceCriteriaGroupType(dto);
        return ImportanceCriteriaGroupTypeFactory.create(entity);
    }

    @Operation(summary = "Обновление типа группы критериев важности", description = "Обновление типа группы критериев важности", tags = {"importancecriteria"})
    @BadRequestAPIResponses
    @PutMapping("/api/importancecriteriagrouptype/update/{id}")
    public ImportanceCriteriaGroupTypeDto updateImportanceCriteriaGroupType(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица importance_criteria_group_type)")
        @RequestBody ImportanceCriteriaGroupTypeDto dto,
        @PathVariable(name = "id") Long id) throws NotFoundException, JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/importancecriteriagrouptype/update/%d", id), null, dto);
        if (id == null) {
            throw new BadRequestException(TextConstants.ID_CANNOT_BE_NULL);
        }
        dto.setId(id);
        ImportanceCriteriaGroupTypeEntity entity = importanceCriteriaService.updateImportanceCriteriaGroupType(dto);
        return ImportanceCriteriaGroupTypeFactory.create(entity);
    }

    @Operation(summary = "Удаление типа группы критериев важности", description = "Удаление типа группы критериев важности", tags = {"importancecriteria"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/importancecriteriagrouptype/delete/{id}")
    @Transactional(rollbackFor = Exception.class)
    public OperationResult deleteImportanceCriteriaGroupType(@PathVariable(name = "id") Long id) throws JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/importancecriteriagrouptype/delete/%d", id), null, null);
        try {
            importanceCriteriaService.deleteImportanceCriteriaGroupType(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, String.format("cannot find any importancecriteriagrouptype with id = %d", id));
        }
    }

    @Operation(summary = "Получение информации о всех группах критериев важности", description = "Получение информации о всех группах критериев важности", tags = {"importancecriteria"})
    @GetMapping("/api/importancecriteriagroup/list")
    public List<ImportanceCriteriaGroupDto> findImportanceCriteriaGroupAll() throws AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        List<ImportanceCriteriaGroupEntity> entities = importanceCriteriaService.findImportanceCriteriaGroupAll();
        return entities.stream()
            .map(ImportanceCriteriaGroupFactory::create)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Получение информации о группе критериев важности", description = "Получение информации о группе критериев важности", tags = {"importancecriteria"})
    @BadRequestAPIResponses
    @GetMapping("/api/importancecriteriagroup/{id}")
    public ImportanceCriteriaGroupDto getImportanceCriteriaGroup(@PathVariable(name = "id") Long id) throws NotFoundException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        ImportanceCriteriaGroupEntity entity = importanceCriteriaService.findImportanceCriteriaGroup(id);
        return ImportanceCriteriaGroupFactory.create(entity);
    }

    @Operation(summary = "Создание группы критериев важности", description = "Создание группы критериев важности", tags = {"importancecriteria"})
    @BadRequestAPIResponses
    @PostMapping("/api/importancecriteriagroup/create")
    public ImportanceCriteriaGroupDto createImportanceCriteriaGroup(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица importance_criteria_group)")
        @RequestBody ImportanceCriteriaGroupDto dto) throws NotFoundException, JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/importancecriteriagroup/create", null, dto);
        ImportanceCriteriaGroupEntity entity = importanceCriteriaService.createImportanceCriteriaGroup(dto);
        return ImportanceCriteriaGroupFactory.create(entity);
    }

    @Operation(summary = "Обновление группы критериев важности", description = "Обновление группы критериев важности", tags = {"importancecriteria"})
    @BadRequestAPIResponses
    @PutMapping("/api/importancecriteriagroup/update/{id}")
    public ImportanceCriteriaGroupDto updateImportanceCriteriaGroup(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица importance_criteria_group)")
        @RequestBody ImportanceCriteriaGroupDto dto,
        @PathVariable(name = "id") Long id) throws NotFoundException, JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/importancecriteriagroup/update/%d", id), null, dto);
        if (id == null) {
            throw new BadRequestException(TextConstants.ID_CANNOT_BE_NULL);
        }
        dto.setId(id);
        ImportanceCriteriaGroupEntity entity = importanceCriteriaService.updateImportanceCriteriaGroup(dto);
        return ImportanceCriteriaGroupFactory.create(entity);
    }

    @Operation(summary = "Удаление группы критериев важности", description = "Удаление группы критериев важности", tags = {"importancecriteria"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/importancecriteriagroup/delete/{id}")
    @Transactional(rollbackFor = Exception.class)
    public OperationResult deleteImportanceCriteriaGroup(@PathVariable(name = "id") Long id) throws JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/importancecriteriagroup/delete/%d", id), null, null);
        try {
            importanceCriteriaService.deleteImportanceCriteriaGroup(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, String.format("cannot find any importancecriteriagroup with id = %d", id));
        }
    }

    @Operation(summary = "Получение информации о всех критериях важности", description = "Получение информации о всех критериях важности", tags = {"importancecriteria"})
    @GetMapping("/api/importancecriteria/list")
    public List<ImportanceCriteriaDto> getImportanceCriteriaList() {
        List<ImportanceCriteriaEntity> entities = importanceCriteriaService.findImportanceCriteriaAll();
        return entities.stream()
            .map(ImportanceCriteriaFactory::create)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Создание критериев важности", description = "Создание критериев важности", tags = {"importancecriteria"})
    @PostMapping("/api/importancecriteria/create")
    public ImportanceCriteriaDto createImportanceCriteria(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица importance_criteria)")
        @RequestBody ImportanceCriteriaDto dto) throws NotFoundException, JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/importancecriteria/create", null, dto);
        ImportanceCriteriaEntity entity = importanceCriteriaService.createImportanceCriteria(dto);
        return ImportanceCriteriaFactory.create(entity);
    }

    @Operation(summary = "Обновление критериев важности", description = "Обновление критериев важности", tags = {"importancecriteria"})
    @BadRequestAPIResponses
    @PutMapping("/api/importancecriteria/update/{id}")
    public ImportanceCriteriaDto updateImportanceCriteria(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица importance_criteria)")
        @RequestBody ImportanceCriteriaDto dto,
        @PathVariable(name = "id") Long id) throws NotFoundException, JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/importancecriteria/update/%d", id), null, dto);
        if (id == null) {
            throw new BadRequestException(TextConstants.ID_CANNOT_BE_NULL);
        }
        dto.setId(id);
        ImportanceCriteriaEntity entity = importanceCriteriaService.updateImportanceCriteria(dto);
        return ImportanceCriteriaFactory.create(entity);
    }

    @Operation(summary = "Удаление критериев важности", description = "Удаление критериев важности", tags = {"importancecriteria"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/importancecriteria/delete/{id}")
    @Transactional(rollbackFor = Exception.class)
    public OperationResult deleteImportanceCriteria(@PathVariable(name = "id") Long id) throws JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/importancecriteria/delete/%d", id), null, null);
        try {
            importanceCriteriaService.deleteImportanceCriteria(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, String.format("cannot find any importancecriteria with id = %d", id));
        }
    }

    @Operation(summary = "Получение информации о всех позициях критериев важности", description = "Получение информации о всех позициях критериев важности", tags = {"importancecriteria"})
    @GetMapping("/api/positionimportancecriteria/list")
    public List<PositionImportanceCriteriaDto> getPositionImportanceCriteriaList() {
        List<PositionImportanceCriteriaEntity> entities = importanceCriteriaService.findPositionImportanceCriteriaAll();
        return entities.stream()
            .map(PositionImportanceCriteriaFactory::create)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Создание позиции критериев важности", description = "Создание позиции критериев важности", tags = {"importancecriteria"})
    @PostMapping("/api/positionimportancecriteria/create")
    public PositionImportanceCriteriaDto createPositionImportanceCriteria(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_importance_criteria)")
        @RequestBody PositionImportanceCriteriaDto dto) throws NotFoundException, JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/positionimportancecriteria/create", null, dto);
        PositionImportanceCriteriaEntity entity = importanceCriteriaService.createPositionImportanceCriteria(dto);
        return PositionImportanceCriteriaFactory.create(entity);
    }

    @Operation(summary = "Обновление позиции критериев важности", description = "Обновление позиции критериев важности", tags = {"importancecriteria"})
    @BadRequestAPIResponses
    @PutMapping("/api/positionimportancecriteria/update/{id}")
    public PositionImportanceCriteriaDto updatePositionImportanceCriteria(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_importance_criteria)")
        @RequestBody PositionImportanceCriteriaDto dto,
        @PathVariable(name = "id") Long id) throws NotFoundException, JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/positionimportancecriteria/update/%d", id), null, dto);
        if (id == null) {
            throw new BadRequestException(TextConstants.ID_CANNOT_BE_NULL);
        }
        dto.setId(id);
        PositionImportanceCriteriaEntity entity = importanceCriteriaService.updatePositionImportanceCriteria(dto);
        return PositionImportanceCriteriaFactory.create(entity);
    }

    @Operation(summary = "Удаление позиции критериев важности", description = "Удаление позиции критериев важности", tags = {"importancecriteria"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/positionimportancecriteria/delete/{id}")
    @Transactional(rollbackFor = Exception.class)
    public OperationResult deleteReasonType(@PathVariable(name = "id") Long id) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/positionimportancecriteria/delete/%d", id), null, null);
        try {
            importanceCriteriaService.deletePositionImportanceCriteria(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, String.format("cannot find any positionimportancecriteria with id = %d", id));
        }
    }

    @Operation(summary = "Обновление типа причин", description = "Обновление типа причин", tags = {"importancecriteria"})
    @PutMapping("/api/position/recalculateimportancecriteria")
    public List<PositionImportanceCriteriaDto> recalculatePositionImportanceCriteria(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_importance_criteria)")
        @RequestBody RecalculatePositionImportanceCriteriaDto dto) throws NotFoundException {
        List<PositionImportanceCriteriaEntity> result = importanceCriteriaService.recalculateImportanceCriteria(dto.getImportanceCriteriaId(), dto.getPositionId(), dto.getValue());
        return result.stream()
            .map(PositionImportanceCriteriaFactory::create)
            .collect(Collectors.toList());
    }

    protected boolean checkEmployeeHr(Long employeeId) {
        List<LegalEntityTeamAssignmentEntity> legalEntityTeamAssignmentEntities = legalEntityTeamAssignmentService.getLegalEntityTeamAssignments(employeeId);
        for (LegalEntityTeamAssignmentEntity item : legalEntityTeamAssignmentEntities) {
            if (GlobalDefs.HR_ROLE_SET.contains(item.getRole().getSystemRole().getId())) {
                return true;
            }
        }
        return false;
    }
}
