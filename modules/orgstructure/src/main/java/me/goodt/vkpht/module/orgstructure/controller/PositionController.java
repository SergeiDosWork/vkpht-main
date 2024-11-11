package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.module.orgstructure.domain.dao.simple.PositionAssignmentSmp;
import me.goodt.vkpht.common.api.dto.OperationResult;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionIdsDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionCategoryDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionGradeDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionGroupDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionGroupPositionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionImportanceDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionImportanceReasonGroupDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionInputDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionKrLevelDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionPositionGradeDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionPositionImportanceDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionPositionImportanceInputDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionPositionKrLevelDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionRankDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorRawDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorReadinessDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorReadinessRawDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionTypeDto;
import me.goodt.vkpht.module.orgstructure.api.dto.WorkplaceDto;
import com.goodt.drive.rtcore.dto.rostalent.position.PositionListRequest;
import com.goodt.drive.rtcore.dto.rostalent.position.PositionListResponse;
import com.goodt.drive.rtcore.dto.tasksetting2.FilterAwarePageResponse;
import me.goodt.vkpht.common.api.exception.BadRequestException;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionAssignmentFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionCategoryFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionGradeFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionGroupFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionGroupPositionFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionImportanceFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionImportanceReasonGroupFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionKrLevelFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionPositionGradeFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionPositionImportanceFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionPositionKrLevelFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionRankFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionSuccessorFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionSuccessorReadinessFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionTypeFactory;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionCategoryEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGradeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGroupEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGroupPositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceReasonGroupEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionKrLevelEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionPositionGradeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionPositionImportanceEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionPositionKrLevelEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionRankEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorReadinessEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionTypeEntity;
import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.common.api.LoggerService;
import com.goodt.drive.rtcore.service.notification.NotificationService;
import me.goodt.vkpht.module.orgstructure.api.CalculationRiskService;
import me.goodt.vkpht.module.orgstructure.api.PositionService;
import me.goodt.vkpht.module.orgstructure.api.WorkplaceService;
import me.goodt.vkpht.common.application.util.TextConstants;

@RestController
@GeneralAPIResponses
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;
    private final LoggerService loggerService;
    private final CalculationRiskService calculationRiskService;
    private final NotificationService notificationService;
    private final WorkplaceService workplaceService;
    private final AuthService authService;

    @Operation(summary = "Получение информации о всех актуальных типах позиций",
        description = "Получение информации о всех актуальных типах позиций",
        tags = {"position"})
    @SurProtected(operation = SurOperation.UNIT)
    @GetMapping("/api/position_type/list")
    public List<PositionTypeDto> getPositionTypeList() {
        List<PositionTypeEntity> entities = positionService.findPositionTypeAll();
        return entities.stream().map(PositionTypeFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Создание типа позиции", description = "Создание типа позиции", tags = {"position"})
    @BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
    @PostMapping("/api/position_type/create")
    public PositionTypeDto createPositionType(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_type)")
            @RequestBody PositionTypeDto dto) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/position_type/create", null, dto);
        PositionTypeEntity entity = positionService.createPositionType(dto);
        return PositionTypeFactory.create(entity);
    }

    @Operation(summary = "Обновление типа позиции", description = "Обновление типа позиции", tags = {"position"})
    @BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
    @PutMapping("/api/position_type/update/{id}")
    public PositionTypeDto updatePositionType(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_type)")
            @RequestBody PositionTypeDto dto,
            @PathVariable(name = "id") Long id) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/position_type/update/%d", id), null, dto);
        if (id == null) {
            throw new BadRequestException(TextConstants.ID_CANNOT_BE_NULL);
        }
        dto.setId(id);
        PositionTypeEntity entity = positionService.updatePositionType(dto);
        return PositionTypeFactory.create(entity);
    }

    @Operation(summary = "Закрытые типа позиции",
        description = "Закрытие типа позиции", tags = {"position"})
    @BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
    @DeleteMapping("/api/position_type/delete/{id}")
    public OperationResult deletePositionType(@PathVariable(name = "id") Long id) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/position_type/delete/%d", id), null, null);
        try {
            positionService.deletePositionType(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, "cannot find any position_type with id = " + id);
        }
    }

    @Operation(summary = "Получение информации о всех уровнях", description = "Получение информации о всех уровнях", tags = {"position"})
    @GetMapping("/api/position_kr_level/list")
    @SurProtected(operation = SurOperation.UNIT)
    public List<PositionKrLevelDto> getPositionKrLevelList() {
        List<PositionKrLevelEntity> entities = positionService.findPositionKrLevelAll();
        return entities.stream().map(PositionKrLevelFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Создание уровня", description = "Создание уровня", tags = {"position"})
    @PostMapping("/api/position_kr_level/create")
    public PositionKrLevelDto createPositionKrLevel(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_kr_level)")
            @RequestBody PositionKrLevelDto dto) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/position_kr_level/create", null, dto);
        PositionKrLevelEntity entity = positionService.createPositionKrLevel(dto);
        return PositionKrLevelFactory.create(entity);
    }

    @Operation(summary = "Обновление уровня", description = "Обновление уровня", tags = {"position"})
    @BadRequestAPIResponses
    @PutMapping("/api/position_kr_level/update/{id}")
    @SurProtected(operation = SurOperation.UNIT)
    public PositionKrLevelDto updatePositionKrLevel(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_kr_level)")
            @RequestBody PositionKrLevelDto dto,
            @PathVariable(name = "id") Long id) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/position_kr_level/update/%d", id), null, dto);
        if (id == null) {
            throw new BadRequestException(TextConstants.ID_CANNOT_BE_NULL);
        }
        dto.setId(id);
        PositionKrLevelEntity entity = positionService.updatePositionKrLevel(dto);
        return PositionKrLevelFactory.create(entity);
    }

    @Operation(summary = "Удаление уровня", description = "Удаление уровня", tags = {"position"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/position_kr_level/delete/{id}")
    @SurProtected(operation = SurOperation.UNIT)
    public OperationResult deletePositionKrLevel(@PathVariable(name = "id") Long id) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/position_kr_level/delete/%d", id), null, null);
        try {
            positionService.deletePositionKrLevel(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, "cannot find any position_kr_level with id = " + id);
        }
    }

    @Operation(summary = "Получение информации о связях позиций с уровнями", description = "Получение информации о связях позиций с уровнями", tags = {"position"})
    @BadRequestAPIResponses
    @GetMapping("/api/position_position_kr_level/{position_id}/{position_kr_level_id}")
    @SurProtected(operation = SurOperation.UNIT)
    public PositionPositionKrLevelDto getPositionPositionKrLevel(
            @Parameter(name = "position_id", description = "Идентификатор профиля позиции (таблица position_position_kr_level).", example = "1")
            @PathVariable(name = "position_id") Long positionId,
            @Parameter(name = "position_kr_level_id", description = "Идентификатор уровня (таблица position_position_kr_level).", example = "1")
            @PathVariable(name = "position_kr_level_id") Long positionKrLevelId) throws NotFoundException {
        PositionPositionKrLevelEntity entity = positionService.findPositionPositionKrLevel(positionId, positionKrLevelId);
        return PositionPositionKrLevelFactory.create(entity);
    }

    @Operation(summary = "Создание связи позиций с уровнями", description = "Создание связи позиций с уровнями", tags = {"position"})
    @PostMapping("/api/position_position_kr_level/create")
    @SurProtected(operation = SurOperation.UNIT)
    public PositionPositionKrLevelDto createPositionPositionKrLevel(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_position_kr_level)")
            @RequestBody PositionPositionKrLevelDto dto) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/position_position_kr_level/create", null, dto);
        PositionPositionKrLevelEntity entity = positionService.createPositionPositionKrLevel(dto);
        return PositionPositionKrLevelFactory.create(entity);
    }

    @Operation(summary = "Удаление связи позиций с уровнями", description = "Удаление связи позиций с уровнями", tags = {"position"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/position_position_kr_level/delete/{position_id}/{position_kr_level_id}")
    @SurProtected(operation = SurOperation.UNIT)
    public OperationResult deletePositionPositionKrLevel(
            @Parameter(name = "position_id", description = "Идентификатор профиля позиции (таблица position_position_kr_level).", example = "1")
            @PathVariable(name = "position_id") Long positionId,
            @Parameter(name = "position_kr_level_id", description = "Идентификатор уровня (таблица position_position_kr_level).", example = "1")
            @PathVariable(name = "position_kr_level_id") Long positionKrLevelId) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/position_position_kr_level/delete/%d/%d", positionId, positionKrLevelId), null, null);
        try {
            positionService.deletePositionPositionKrLevel(positionId, positionKrLevelId);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, String.format("cannot find any position_position_kr_level with position id = %d and position_kr_level id = %d", positionId, positionKrLevelId));
        }
    }

    @Operation(summary = "Получение информации о всех группах", description = "Получение информации о всех группах", tags = {"position"})
    @SurProtected(operation = SurOperation.UNIT)
    @GetMapping("/api/position_group/list")
    public List<PositionGroupDto> getPositionGroupList() {
        List<PositionGroupEntity> entities = positionService.findPositionGroupAll();
        return entities.stream().map(PositionGroupFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Создание группы", description = "Создание группы", tags = {"position"})
    @SurProtected(operation = SurOperation.UNIT)
    @PostMapping("/api/position_group/create")
    public PositionGroupDto createPositionGroup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_group)")
            @RequestBody @Valid PositionGroupDto dto) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/position_group/create", null, dto);
        PositionGroupEntity entity = positionService.createPositionGroup(dto);
        return PositionGroupFactory.create(entity);
    }

    @Operation(summary = "Обновление группы", description = "Обновление группы", tags = {"position"})
    @BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
    @PutMapping("/api/position_group/update/{id}")
    public PositionGroupDto updatePositionGroup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_group)")
            @RequestBody @Valid PositionGroupDto dto,
            @PathVariable(name = "id") Long id) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/position_group/update/%d", id), null, dto);
        if (id == null) {
            throw new BadRequestException(TextConstants.ID_CANNOT_BE_NULL);
        }
        dto.setId(id);
        PositionGroupEntity entity = positionService.updatePositionGroup(dto);
        return PositionGroupFactory.create(entity);
    }

    @Operation(summary = "Удаление группы", description = "Удаление группы", tags = {"position"})
    @BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
    @DeleteMapping("/api/position_group/delete/{id}")
    public OperationResult deletePositionGroup(@PathVariable(name = "id") Long id) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/position_group/delete/%d", id), null, null);
        try {
            positionService.deletePositionGroup(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, "cannot find any position_group with id = " + id);
        }
    }

    @Operation(summary = "Получение информации о связях позиций с группами", description = "Получение информации о связях позиций с группами", tags = {"position"})
    @GetMapping("/api/position_group_position")
    public List<PositionGroupPositionDto> getPositionGroupPositionList(
            @Parameter(name = "position_id", description = "Идентификатор профиля позиции (таблица position).", example = "1")
            @RequestParam(name = "position_id", required = false) Long positionId,
            @Parameter(name = "position_group_id", description = "Идентификатор уровня (таблица position_group).", example = "1")
            @RequestParam(name = "position_group_id", required = false) Long positionGroupId) {
        List<PositionGroupPositionEntity> positionGroupPositionEntities = positionService.findPositionGroupPositionAll(positionId, positionGroupId);
        return positionGroupPositionEntities.stream().map(PositionGroupPositionFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Создание связи позиций с уровнями", description = "Создание связи позиций с уровнями", tags = {"position"})
    @SurProtected(operation = SurOperation.UNIT)
    @PostMapping("/api/position_group_position/create")
    public PositionGroupPositionDto createPositionGroupPosition(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_group_position)")
            @RequestBody PositionGroupPositionDto dto) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/position_group_position/create", null, dto);
        PositionGroupPositionEntity entity = positionService.createPositionGroupPosition(dto);
        return PositionGroupPositionFactory.create(entity);
    }

    @Operation(summary = "Удаление связи позиций с уровнями", description = "Удаление связи позиций с уровнями", tags = {"position"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/position_group_position/delete/{id}")
    public OperationResult deletePositionGroupPosition(
            @Parameter(name = "id", description = "Идентификатор связи позиций с уровнями (таблица position_group_position).", example = "1")
            @PathVariable(name = "id") Long id) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/position_group_position/delete/%d", id), null, null);
        try {
            positionService.deletePositionGroupPosition(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, String.format("cannot find any position_group_position with id = %d", id));
        }
    }

    @Operation(summary = "Получение информации о всех грейдах позиций", description = "Получение информации о всех грейдах позиций", tags = {"position"})
    @SurProtected(operation = SurOperation.UNIT)
    @GetMapping("/api/position_grade/list")
    public List<PositionGradeDto> getPositionGradeList() {
        List<PositionGradeEntity> entities = positionService.findPositionGradeAll();
        return entities.stream().map(PositionGradeFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Создание грейда позиции", description = "Создание грейда позиции", tags = {"position"})
    @SurProtected(operation = SurOperation.UNIT)
    @PostMapping("/api/position_grade/create")
    public PositionGradeDto createPositionGrade(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_grade)")
            @RequestBody PositionGradeDto dto) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/position_grade/create", null, dto);
        PositionGradeEntity entity = positionService.createPositionGrade(dto);
        return PositionGradeFactory.create(entity);
    }

    @Operation(summary = "Обновление грейда позиции", description = "Обновление грейда позиции", tags = {"position"})
    @BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
    @PutMapping("/api/position_grade/update/{id}")
    public PositionGradeDto updatePositionGrade(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_grade)")
            @RequestBody PositionGradeDto dto,
            @PathVariable(name = "id") Long id) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/position_grade/update/%d", id), null, dto);
        if (id == null) {
            throw new BadRequestException(TextConstants.ID_CANNOT_BE_NULL);
        }
        dto.setId(id);
        PositionGradeEntity entity = positionService.updatePositionGrade(dto);
        return PositionGradeFactory.create(entity);
    }

    @Operation(summary = "Удаление грейда позиции", description = "Удаление грейда позиции", tags = {"position"})
    @BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
    @DeleteMapping("/api/position_grade/delete/{id}")
    public OperationResult deletePositionGrade(@PathVariable(name = "id") Long id) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/position_grade/delete/%d", id), null, null);
        try {
            positionService.deletePositionGrade(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, "cannot find any position_grade with id = " + id);
        }
    }

    @Operation(summary = "Получение информации о связях позиций с грейдами", description = "Получение информации о связях позиций с грейдами", tags = {"position"})
    @BadRequestAPIResponses
    @GetMapping("/api/position_position_grade/{position_id}/{position_grade_id}")
    public PositionPositionGradeDto getPositionPositionGrade(
            @Parameter(name = "position_id", description = "Идентификатор профиля позиции (таблица position_position_grade).", example = "1")
            @PathVariable(name = "position_id") Long positionId,
            @Parameter(name = "position_grade_id", description = "Идентификатор грейда (таблица position_position_grade).", example = "1")
            @PathVariable(name = "position_grade_id") Long positionGradeId) throws NotFoundException {
        PositionPositionGradeEntity entity = positionService.findPositionPositionGrade(positionId, positionGradeId);
        return PositionPositionGradeFactory.create(entity);
    }

    @Operation(summary = "Создание связи позиций с грейдами", description = "Создание связи позиций с грейдами", tags = {"position"})
    @PostMapping("/api/position_position_grade/create")
    public PositionPositionGradeDto createPositionPositionGrade(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_position_grade)")
            @RequestBody PositionPositionGradeDto dto) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/position_position_grade/create", null, dto);
        PositionPositionGradeEntity entity = positionService.createPositionPositionGrade(dto);
        return PositionPositionGradeFactory.create(entity);
    }

    @Operation(summary = "Удаление связи позиций с грейдами", description = "Удаление связи позиций с грейдами", tags = {"position"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/position_position_grade/delete/{position_id}/{position_grade_id}")
    public OperationResult deletePositionPositionGrade(
            @Parameter(name = "position_id", description = "Идентификатор профиля позиции (таблица position_position_grade).", example = "1")
            @PathVariable(name = "position_id") Long positionId,
            @Parameter(name = "position_grade_id", description = "Идентификатор уровня (таблица position_position_grade).", example = "1")
            @PathVariable(name = "position_grade_id") Long positionGradeId) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/position_position_grade/delete/%d/%d", positionId, positionGradeId), null, null);
        try {
            positionService.deletePositionPositionGrade(positionId, positionGradeId);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, String.format("cannot find any position_position_grade with position id = %d and position_grade id = %d", positionId, positionGradeId));
        }
    }

    @Operation(summary = "Получение информации о группах критериев критичности", description = "Получение информации о группах критериев критичности", tags = {"position"})
    @GetMapping("/api/position_importance_reason_group/list")
    public List<PositionImportanceReasonGroupDto> getPositionImportanceReasonGroupList() {
        List<PositionImportanceReasonGroupEntity> entities = positionService.findPositionImportanceReasonGroupAll();
        return entities.stream().map(PositionImportanceReasonGroupFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Создание группы критериев критичности", description = "Создание группы критериев критичности", tags = {"position"})
    @PostMapping("/api/position_importance_reason_group/create")
    public PositionImportanceReasonGroupDto createPositionImportanceReasonGroup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_importance_reason_group)")
            @RequestBody PositionImportanceReasonGroupDto dto) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/position_importance_reason_group/create", null, dto);
        PositionImportanceReasonGroupEntity entity = positionService.createPositionImportanceReasonGroup(dto);
        return PositionImportanceReasonGroupFactory.create(entity);
    }

    @Operation(summary = "Обновление группы критериев критичности", description = "Обновление группы критериев критичности", tags = {"position"})
    @BadRequestAPIResponses
    @PutMapping("/api/position_importance_reason_group/update/{id}")
    public PositionImportanceReasonGroupDto updatePositionImportanceReasonGroup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_importance_reason_group)")
            @RequestBody PositionImportanceReasonGroupDto dto,
            @PathVariable(name = "id") Long id) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/position_importance_reason_group/update/%d", id), null, dto);
        if (id == null) {
            throw new BadRequestException(TextConstants.ID_CANNOT_BE_NULL);
        }
        dto.setId(id);
        PositionImportanceReasonGroupEntity entity = positionService.updatePositionImportanceReasonGroup(dto);
        return PositionImportanceReasonGroupFactory.create(entity);
    }

    @Operation(summary = "Удаление группы критериев критичности", description = "Удаление группы критериев критичности", tags = {"position"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/position_importance_reason_group/delete/{id}")
    public OperationResult deletePositionImportanceReasonGroup(@PathVariable(name = "id") Long id) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/position_importance_reason_group/delete/%d", id), null, null);
        try {
            positionService.deletePositionImportanceReasonGroup(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, "cannot find any position_importance_reason_group with id = " + id);
        }
    }

    @Operation(summary = "Получение информации о position successor", description = "Получение информации о position successor", tags = {"position"})
    @GetMapping("/api/position_successor/list")
    public List<PositionSuccessorDto> getPositionSuccessorList() {
        List<PositionSuccessorEntity> entities = positionService.findPositionSuccessorAll();
        return entities.stream().map(PositionSuccessorFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Получение информации о position successor по идентификатору", description = "Получение информации о position successor по идентификатору", tags = {"position"})
    @BadRequestAPIResponses
    @GetMapping("/api/position_successor/{id}")
    public PositionSuccessorDto getPositionSuccessor(
            @Parameter(name = "id", description = "Идентификатор position successor (см. таблицу position_successor)", example = "4")
            @PathVariable("id") Long id) throws NotFoundException {
        return PositionSuccessorFactory.create(positionService.getPositionSuccessorById(id));
    }

    @Operation(summary = "Создание position successor", description = "Создание position successor", tags = {"position"})
    @SurProtected(operation = SurOperation.UNIT)
    @PostMapping("/api/position_successor/create")
    public PositionSuccessorDto createPositionSuccessor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_successor)")
            @RequestBody PositionSuccessorRawDto dto) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/position_successor/create", null, dto);
        PositionSuccessorEntity entity = positionService.createPositionSuccessor(dto);

        notificationService.baseNotification(null, TextConstants.CODE_39, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, entity.getId()));

        if (entity.getReasonExclusion() != null && entity.getReasonExclusion().getId().equals(1L)) {
            notificationService.baseNotification(null, TextConstants.CODE_164, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO, entity.getId(), TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, entity.getId()));
            notificationService.baseNotification(null, TextConstants.CODE_165, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO, entity.getId(), TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, entity.getId()));
        }
        if (dto.getDateCommitHr() != null) {
            notificationService.baseNotification(null, TextConstants.CODE_6, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, entity.getId(), TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_INFO, entity.getId(), TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO, entity.getId()));
            notificationService.baseNotification(null, TextConstants.CODE_7, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_INFO, entity.getId()));
        }
        return PositionSuccessorFactory.create(entity);
    }

    @Operation(summary = "Обновление position successor", description = "Обновление position successor", tags = {"position"})
    @BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
    @PutMapping("/api/position_successor/update/{id}")
    public PositionSuccessorDto updatePositionSuccessor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_successor)")
            @RequestBody PositionSuccessorRawDto dto,
            @PathVariable(name = "id") Long id) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/position_successor/update/%d", id), null, dto);
        PositionSuccessorEntity entity = positionService.findPositionSuccessor(id);
        Date currentDateCommitHr = entity.getDateCommitHr();
        Date currentDatePriority = entity.getDatePriority();

        PositionSuccessorEntity updatedEntity = positionService.updatePositionSuccessor(id, dto);

        if (dto.getDateCommitHr() != null && dto.getDatePriority() != null) {
            if ((currentDateCommitHr == null || !currentDateCommitHr.equals(dto.getDateCommitHr())) &&
                    (currentDatePriority == null || !currentDatePriority.equals(dto.getDatePriority()))) {
                notificationService.baseNotification(null, TextConstants.CODE_170, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, entity.getId()));
            }
        }

        if (dto.getDateCommitHr() != null) {
            notificationService.baseNotification(null, TextConstants.CODE_6, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, entity.getId(), TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_INFO, entity.getId(), TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO, entity.getId()));
            notificationService.baseNotification(null, TextConstants.CODE_7, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_INFO, entity.getId()));
        }

        notificationService.baseNotification(null, TextConstants.CODE_37, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, entity.getId()));
        return PositionSuccessorFactory.create(updatedEntity);
    }

    @Operation(summary = "Удаление position successor", description = "Удаление position successor", tags = {"position"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/position_successor/delete/{id}")
    public OperationResult deletePositionSuccessor(@PathVariable(name = "id") Long id) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/position_successor/delete/%d", id), null, null);

        try {
            positionService.deletePositionSuccessor(id);

            notificationService.baseNotification(null, TextConstants.CODE_9, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, id, TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_INFO, id, TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO, id));
            notificationService.baseNotification(null, TextConstants.CODE_38, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, id));
            notificationService.baseNotification(null, TextConstants.CODE_168, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO, id, TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, id));
            notificationService.baseNotification(null, TextConstants.CODE_169, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO, id, TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, id));
            notificationService.baseNotification(null, TextConstants.CODE_195, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, id));
            notificationService.baseNotification(null, TextConstants.CODE_196, Map.of(TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO, id, TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_INFO, id));

            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, "cannot find any position_successor with id = " + id);
        }
    }

    @Operation(summary = "Получение информации о position successor readiness", description = "Получение информации о position successor readiness", tags = {"position"})
    @GetMapping("/api/position_successor_readiness/list")
    public List<PositionSuccessorReadinessDto> getPositionSuccessorReadinessList() {
        List<PositionSuccessorReadinessEntity> entities = positionService.findPositionSuccessorReadinessAll();
        return entities.stream().map(PositionSuccessorReadinessFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Получение информации о position successor readiness по идентификатору", description = "Получение информации о position successor readiness по идентификатору", tags = {"position"})
    @BadRequestAPIResponses
    @GetMapping("/api/position_successor_readiness/{id}")
    public PositionSuccessorReadinessDto getPositionSuccessorReadiness(
            @Parameter(name = "id", description = "Идентификатор position successor readiness (см. таблицу position_successor_readiness)", example = "4")
            @PathVariable("id") Long id) throws NotFoundException {
        return PositionSuccessorReadinessFactory.create(positionService.getPositionSuccessorReadinessById(id));
    }

    @Operation(summary = "Создание position successor readiness", description = "Создание position successor readiness", tags = {"position"})
    @PostMapping("/api/position_successor_readiness/create")
    public PositionSuccessorReadinessDto createPositionSuccessorReadiness(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_successor_readiness)")
            @RequestBody PositionSuccessorReadinessRawDto dto) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/position_successor_readiness/create", null, dto);
        PositionSuccessorReadinessEntity entity = positionService.createPositionSuccessorReadiness(dto);

        if (entity.getReadiness() != null && entity.getReadiness().getId().equals(1)) {
            notificationService.baseNotification(null, TextConstants.CODE_166, Map.of(TextConstants.POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_SUCCESSOR_READINESS_INFO, entity.getId(), TextConstants.POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_INFO, entity.getId()));
            notificationService.baseNotification(null, TextConstants.CODE_167, Map.of(TextConstants.POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_SUCCESSOR_READINESS_INFO, entity.getId(), TextConstants.POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_INFO, entity.getId()));
        }

        return PositionSuccessorReadinessFactory.create(entity);
    }

    @Operation(summary = "Обновление position successor readiness", description = "Обновление position successor readiness", tags = {"position"})
    @BadRequestAPIResponses
    @PutMapping("/api/position_successor_readiness/update/{id}")
    public PositionSuccessorReadinessDto updatePositionSuccessorReadiness(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица position_successor_readiness)")
            @RequestBody PositionSuccessorReadinessRawDto dto,
            @PathVariable(name = "id") Long id) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/position_successor_readiness/update/%d", id), null, dto);
        if (id == null) {
            throw new BadRequestException(TextConstants.ID_CANNOT_BE_NULL);
        }
        dto.setId(id);
        PositionSuccessorReadinessEntity entity = positionService.updatePositionSuccessorReadiness(dto);
        return PositionSuccessorReadinessFactory.create(entity);
    }

    @Operation(summary = "Удаление position successor readiness", description = "Удаление position successor readiness", tags = {"position"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/position_successor_readiness/delete/{id}")
    public OperationResult deletePositionSuccessorReadiness(@PathVariable(name = "id") Long id) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/position_successor_readiness/delete/%d", id), null, null);
        try {
            positionService.deletePositionSuccessorReadiness(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, "cannot find any position_successor_readiness with id = " + id);
        }
    }

    @Operation(summary = "Получение информации о всех категориях позиций", description = "Получение информации о всех категориях позиций", tags = {"position"})
    @SurProtected(operation = SurOperation.UNIT)
    @GetMapping("/api/positioncategory")
    public List<PositionCategoryDto> getPositionCategoryList() {
        List<PositionCategoryEntity> entities = positionService.findPositionCategoryAll();
        return entities.stream().map(PositionCategoryFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Получение информации о всех рангах позиций", description = "Получение информации о всех рангах позиций", tags = {"position"})
    @SurProtected(operation = SurOperation.UNIT)
    @GetMapping("/api/positionrank")
    public List<PositionRankDto> getPositionRankist() {
        List<PositionRankEntity> entities = positionService.findPositionRankAll();
        return entities.stream().map(PositionRankFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Обновление Position", description = "Обновление Position", tags = {"position"})
    @BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
    @PutMapping("/api/position/update/{id}")
    public PositionDto updatePosition(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление position (таблица position)")
            @RequestBody PositionInputDto dto,
            @PathVariable(name = "id") Long id) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/position/update/%d", id), null, dto);

        PositionEntity position = positionService.updatePosition(id, dto);
        return PositionFactory.create(position);
    }

    @Operation(summary = "Получение списка Position по идентификатору сотрудника и идентификатору подразделения", description = "Получение списка Position по идентификатору сотрудника и идентификатору подразделения", tags = {"position"})
    @GetMapping("/api/position/find")
    public List<PositionDto> getPositionAssignmentByEmployeeId(
            @Parameter(name = "employee_id", description = "Идентификатор сотрудника (таблица position_assignment)", example = "3")
            @RequestParam(name = "employee_id", required = false) Long employeeId,
            @Parameter(name = "division_id", description = "Массив идентификаторов подразделения (таблица division).", example = "[1,2,3]")
            @RequestParam(name = "division_id", required = false) List<Long> divisionIds) {
        return positionService.getPositionByEmployeeIdAndDivisionIds(employeeId, divisionIds)
                .stream()
                .map(PositionFactory::create)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Получение списка Position по идентификатору сотрудника и идентификатору подразделения", description = "Получение списка Position по идентификатору сотрудника и идентификатору подразделения", tags = {"position"})
    @PostMapping("/api/position/find")
    public List<PositionDto> findPositionAssignmentByEmployeeId(
            @Parameter(name = "employee_id", description = "Идентификатор сотрудника (таблица position_assignment)", example = "3")
            @RequestParam(name = "employee_id", required = false) Long employeeId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Объект, содержащий массив идентификаторов подразделения (таблица division).")
            @RequestBody DivisionIdsDto dto) {
        return positionService.getPositionByEmployeeIdAndDivisionIds(employeeId, dto.getDivisionIds())
                .stream()
                .map(PositionFactory::create)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Метод расчета рисков", description = "Метод расчета рисков", tags = {"position"})
    @PostMapping("/api/positionimportancecriteria/calculation")
    public OperationResult calculationRisk(
            @Parameter(name = "legal_entity_id", description = "Идентификатор организации (таблица legal_entity).", example = "1")
            @RequestParam(name = "legal_entity_id") Long legalEntityId) throws NotFoundException {
        calculationRiskService.calculationRisk(legalEntityId);
        return new OperationResult(true, StringUtils.EMPTY);
    }

    @Operation(summary = "Получение Position по идентификатору", description = "Получение Position по идентификатору", tags = {"position"})
    @BadRequestAPIResponses
    @GetMapping("/api/position/{id}")
    public PositionDto getPosition(
            @Parameter(name = "id", description = "Идентификатор Position (см. таблицу position)", example = "7")
            @PathVariable("id") Long positionId) throws NotFoundException {
        return PositionFactory.create(positionService.getPosition(positionId));
    }

    @Operation(summary = "Получение PositionAssignment по идентификатору Position", description = "Получение PositionAssignment по идентификатору Position", tags = {"position"})
    @GetMapping("/api/positionassignment")
    public PositionAssignmentDto getPositionAssignmentByPositionId(
            @Parameter(name = "position_id", description = "Идентификатор объекта Position (см. таблицу position_assignment)", example = "3")
            @RequestParam(name = "position_id") Long positionId) {
        return PositionAssignmentFactory.create(positionService.getPositionAssignmentByPositionId(positionId));
    }

    @Operation(summary = "Получение PositionAssignment для менеджера по division_team", description = "Получение PositionAssignment для менеджера по division_team", tags = {"position"})
    @GetMapping("/api/position-assignment/managed")
    public List<PositionAssignmentSmp> getPositionAssignmentForManager(@RequestParam(name = "division_team", required = false) Long divisionTeamId,
                                                                       @RequestParam(name = "with_children", required = false) boolean withChildren) {
        return positionService.getAllByDivision(divisionTeamId, withChildren);
    }

    @Operation(summary = "Получение информации о всех значениях позиций", description = "Получение информации о всех значениях позиций", tags = {"position"})
    @GetMapping("/api/position_importance/list")
    public List<PositionImportanceDto> getPositionImportanceList() {
        List<PositionImportanceEntity> entities = positionService.findPositionImportanceAll();
        return entities.stream().map(PositionImportanceFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Получение информации значении позиции по идентификатору", description = "Получение информации значении позиции по идентификатору", tags = {"position"})
    @BadRequestAPIResponses
    @GetMapping("/api/position_importance/{id}")
    public PositionImportanceDto getPositionImportance(
            @Parameter(name = "id", description = "Идентификатор значения позиции (таблица position_importance).", example = "3")
            @PathVariable("id") Integer id) throws NotFoundException {
        PositionImportanceEntity entity = positionService.getPositionImportance(id);
        return PositionImportanceFactory.create(entity);
    }

    @Operation(summary = "Получение связки позиции и критичности позиции.", description = "Получение связки позиции и критичности позиции.", tags = {"position"})
    @BadRequestAPIResponses
    @GetMapping("/api/position_position_importance/{id}")
    public PositionPositionImportanceDto getPositionPositionImportance(
            @Parameter(name = "id", description = "Идентификатор значения позиции (таблица position_importance).", example = "3")
            @PathVariable("id") Long id) throws NotFoundException {
        PositionPositionImportanceEntity entity = positionService.getPositionPositionImportance(id);
        return PositionPositionImportanceFactory.create(entity);
    }

    @Operation(summary = "Получение всех связок позиции и критичности позиции.", description = "Получение всех связок позиции и критичности позиции.", tags = {"position"})
    @GetMapping("/api/position_position_importance")
    public List<PositionPositionImportanceDto> getAllPositionPositionImportance(
            @Parameter(name = "with_closed", description = "Запрашиваются ли закрытые записи.", example = "true")
            @RequestParam(name = "with_closed", required = false, defaultValue = "false") boolean withClosed,
            @Parameter(name = "position_id", description = "Массив идентификаторов должностей (таблица position).", example = "[1, 2, 3]")
            @RequestParam(name = "position_id", required = false) List<Long> positionIds,
            @Parameter(name = "division_team", description = "Фильтрация записей по division_team", example = "[1, 2, 3]")
            @RequestParam(name = "division_team", required = false) List<Long> divisionTeamIds,
            @Parameter(name = "users", description = "Фильтрация записей по division_team_assigment", example = "[1, 2, 3]")
            @RequestParam(name = "users", required = false) List<Long> userIds) {
        return positionService.getAllPositionPositionImportance(withClosed, divisionTeamIds, userIds, positionIds).stream()
                .map(PositionPositionImportanceFactory::create)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Обновление связки позиции и критичности позиции.", description = "Обновление связки позиции и критичности позиции.", tags = {"position"})
    @BadRequestAPIResponses
    @PutMapping("/api/position_position_importance/update/{id}")
    public PositionPositionImportanceDto updatePositionPositionImportance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление position_position_importance (таблица position)")
            @RequestBody PositionPositionImportanceInputDto dto,
            @PathVariable(name = "id") Long id) throws NotFoundException {
        String employeeId = authService.getCurrentUser().getEmployeeExternalId();
        return PositionPositionImportanceFactory.create(positionService.updatePositionPositionImportance(id, dto, employeeId));
    }

    @Operation(summary = "Создание связки позиции и критичности позиции.", description = "Создание связки позиции и критичности позиции.", tags = {"position"})
    @PostMapping("/api/position_position_importance/create")
    public PositionPositionImportanceDto createPositionPositionImportance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление position_position_importance (таблица position)")
            @RequestBody PositionPositionImportanceInputDto dto) throws NotFoundException {
        String employeeId = authService.getCurrentUser().getEmployeeExternalId();
        return PositionPositionImportanceFactory.create(positionService.createPositionPositionImportance(dto, employeeId));
    }

    @Operation(summary = "Удаление связки позиции и критичности позиции.", description = "Удаление связки позиции и критичности позиции.", tags = {"position"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/position_position_importance/delete/{id}")
    public OperationResult deletePositionPositionImportance(
            @PathVariable(name = "id") Long id) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/position_position_importance/delete/%d", id), null, null);
        try {
            positionService.deletePositionPositionImportance(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, e.getMessage());
        }
    }

    @Operation(summary = "Получение информации об актуальных локациях работ по workplace id",
            description = "Получение информации об актуальных локациях работ по workplace id", tags = {"location"})
    @SurProtected(operation = SurOperation.UNIT)
    @GetMapping("/api/location_info")
    public List<WorkplaceDto> locationInfoByWorkplaceId(
            @Parameter(name = "workplace ids", description = "идентификаторы из таблицы org_workplace", required = true)
            @RequestParam List<Long> workplaceIds) {
        return workplaceService.getLocationByWorkplaceId(workplaceIds);
    }

    @Operation(summary = "Алгоритм получения перечня позиций",
    description = "Алгоритм получения перечня позиций (бывшая view_tsk2_position_list)", tags = {"BFF. Position list"})
    @PostMapping("api/bff/position/list")
    public FilterAwarePageResponse<PositionListResponse> positionList(@RequestBody PositionListRequest request){
        return positionService.bffPositionList(request);
    }
}

