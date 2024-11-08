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
import me.goodt.vkpht.module.orgstructure.api.dto.ReasonDto;
import me.goodt.vkpht.module.orgstructure.api.dto.ReasonTypeDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.ReasonFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.ReasonTypeFactory;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonTypeEntity;
import com.goodt.drive.rtcore.security.AuthService;
import com.goodt.drive.rtcore.service.logging.ILoggerService;
import me.goodt.vkpht.module.orgstructure.api.ILegalEntityTeamAssignmentService;
import me.goodt.vkpht.module.orgstructure.api.IReasonService;
import me.goodt.vkpht.common.application.util.GlobalDefs;

@RestController
@GeneralAPIResponses
@RequiredArgsConstructor
public class ReasonController {

    private final ILegalEntityTeamAssignmentService legalEntityTeamAssignmentService;
    private final IReasonService reasonService;
    private final ILoggerService loggerService;
    private final AuthService authService;

    @Operation(summary = "Получение информации о всех типах причин", description = "Получение информации о всех типах причин", tags = {"reason"})
    @GetMapping("/api/reasontype/list")
    public List<ReasonTypeDto> getReasonTypeList() throws AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        List<OrgReasonTypeEntity> entities = reasonService.findReasonTypeAll();
        return entities.stream().map(entity -> ReasonTypeFactory.create(entity)).collect(Collectors.toList());
    }

    @Operation(summary = "Создание типа причин", description = "Создание типа причин", tags = {"reason"})
    @BadRequestAPIResponses
    @PostMapping("/api/reasontype/create")
    public ReasonTypeDto createReasonType(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица reason_type)")
            @RequestBody ReasonTypeDto dto) throws NotFoundException, JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/reasontype/create", null, dto);
        OrgReasonTypeEntity entity = reasonService.createReasonType(dto);
        return ReasonTypeFactory.create(entity);
    }

    @Operation(summary = "Обновление типа причин", description = "Обновление типа причин", tags = {"reason"})
    @BadRequestAPIResponses
    @PutMapping("/api/reasontype/update/{id}")
    public ReasonTypeDto updateReasonType(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица reason_type)")
		@RequestBody ReasonTypeDto dto,
		@PathVariable(name = "id") Long id) throws NotFoundException, JsonProcessingException, AccessDeniedException {
		Long employeeId = authService.getUserEmployeeId();
		if (!checkEmployeeHr(employeeId)) {
			throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
		}
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, String.format("PUT /api/reasontype/update/%d", id), null, dto);
		if (id == null) {
			throw new RuntimeException("Id cannot be null");
		}
		dto.setId(id);
        OrgReasonTypeEntity entity = reasonService.updateReasonType(dto);
        return ReasonTypeFactory.create(entity);
    }

    @Operation(summary = "Удаление типа причин", description = "Удаление типа причин", tags = {"reason"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/reasontype/delete/{id}")
    @Transactional(rollbackFor = Exception.class)
    public OperationResult deleteReasonType(@PathVariable(name = "id") Long id) throws JsonProcessingException, AccessDeniedException {
		Long employeeId = authService.getUserEmployeeId();
		if (!checkEmployeeHr(employeeId)) {
			throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
		}
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, String.format("DELETE /api/reasontype/delete/%d", id), null, null);
		try {
			reasonService.deleteReasonType(id);
			return new OperationResult(true, "");
		} catch (NotFoundException e) {
            return new OperationResult(false, "cannot find any reason_type with id = " + id);
        }
    }

    @Operation(summary = "Получение информации о причине", description = "Получение информации о причине", tags = {"reason"})
    @BadRequestAPIResponses
    @GetMapping("/api/reason/{id}")
    public ReasonDto getReason(@PathVariable(name = "id") Long id) throws NotFoundException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        OrgReasonEntity entity = reasonService.findReason(id);
        return ReasonFactory.create(entity);
    }

    @Operation(summary = "Получение информации о всех типах причин", description = "Получение информации о всех типах причин", tags = {"reason"})
    @GetMapping("/api/reason/list")
    public List<ReasonDto> getReasonList() throws AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        List<OrgReasonEntity> entities = reasonService.findReasonAll();
        return entities.stream().map(entity -> ReasonFactory.create(entity)).collect(Collectors.toList());
    }

    @Operation(summary = "Создание причины", description = "Создание причины", tags = {"reason"})
    @BadRequestAPIResponses
    @PostMapping("/api/reason/create")
    public ReasonDto createReason(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица reason)")
            @RequestBody ReasonDto dto) throws NotFoundException, JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/reason/create", null, dto);
        OrgReasonEntity entity = reasonService.createReason(dto);
        return ReasonFactory.create(entity);
    }

    @Operation(summary = "Обновление причины", description = "Обновление причины", tags = {"reason"})
    @BadRequestAPIResponses
    @PutMapping("/api/reason/update/{id}")
    public ReasonDto updateReason(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица reason)")
            @RequestBody ReasonDto dto,
            @PathVariable(name = "id") Long id) throws NotFoundException, JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/reason/update/%d", id), null, dto);
        if (id == null) {
            throw new RuntimeException("Id cannot be null");
        }
        dto.setId(id);
        OrgReasonEntity entity = reasonService.updateReason(dto);
        return ReasonFactory.create(entity);
    }

    @Operation(summary = "Удаление причины", description = "Удаление причины", tags = {"reason"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/reason/delete/{id}")
    @Transactional(rollbackFor = Exception.class)
    public OperationResult deleteReason(@PathVariable(name = "id") Long id) throws JsonProcessingException, AccessDeniedException {
        Long employeeId = authService.getUserEmployeeId();
        if (!checkEmployeeHr(employeeId)) {
            throw new AccessDeniedException(String.format("Employee %d is not assigned as HR", employeeId));
        }
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/reason/delete/%d", id), null, null);
        try {
            reasonService.deleteReason(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, "cannot find any reason with id = " + id);
        }
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
