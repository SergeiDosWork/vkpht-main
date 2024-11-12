package me.goodt.vkpht.module.orgstructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamRoleDao;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleContainerDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleRawDto;
import me.goodt.vkpht.common.api.exception.BadRequestException;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamRoleFactory;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamRoleEntity;
import me.goodt.vkpht.common.api.LoggerService;
import me.goodt.vkpht.module.orgstructure.api.DivisionService;
import me.goodt.vkpht.module.orgstructure.api.DivisionTeamRoleService;
import me.goodt.vkpht.module.orgstructure.api.EmployeeService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.domain.entity.DomainObject;

@Slf4j
@GeneralAPIResponses
@RestController
@RequiredArgsConstructor
public class DivisionTeamRoleController {

    private final DivisionTeamRoleService divisionTeamRoleService;
    private final DivisionTeamRoleDao divisionTeamRoleDao;
    private final DivisionService divisionService;
    private final EmployeeService findService;
    private final LoggerService loggerService;

    @Operation(summary = "Получение информации о роли в команде подразделения", description = "Получение информации о роли в команде подразделения", tags = {"division_team_role"})
    @BadRequestAPIResponses
    @GetMapping("/api/divisionteamrole/{id}")
    public DivisionTeamRoleRawDto get(
        @Parameter(name = "id", description = "Идентификатор роли (таблица division_team_role).", example = "1")
        @PathVariable(name = "id") Long id) throws NotFoundException {
        Optional<DivisionTeamRoleEntity> optional = divisionTeamRoleDao.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException(String.format("Division team role with id %d not found", id));
        }

        return DivisionTeamRoleFactory.createRaw(optional.get());
    }

    @Operation(summary = "Получение информации о всех ролях в команде подразделения", description = "Получение информации о всех ролях в команде подразделения", tags = {"division_team_role"})
    @GetMapping("/api/divisionteamrole")
    public List<DivisionTeamRoleRawDto> get() {
        List<DivisionTeamRoleRawDto> result = new ArrayList<>();
        for (DivisionTeamRoleEntity item : divisionTeamRoleDao.findAll()) {
            result.add(DivisionTeamRoleFactory.createRaw(item));
        }
        return result;
    }

    @Operation(summary = "Создание роли в команде подразделения", description = "Создание роли в команде подразделения", tags = {"division_team_role"})
    @BadRequestAPIResponses
    @PostMapping("/api/divisionteamrole")
    public DivisionTeamRoleDto create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица division_team_role)")
        @RequestBody DivisionTeamRoleRawDto dto) throws NotFoundException, JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/divisionteamrole", null, dto);
        DivisionTeamRoleEntity entity = divisionTeamRoleService.create(dto);
        return DivisionTeamRoleFactory.create(entity);
    }

    @Operation(summary = "Изменение роли в команде подразделения", description = "Изменение роли в команде подразделения по идентификатору роли", tags = {"division_team_role"})
    @BadRequestAPIResponses
    @PutMapping("/api/divisionteamrole/{id}")
    public DivisionTeamRoleDto update(
        @Parameter(name = "id", description = "Идентификатор роли в команде подразделения (таблица division_team_role).", example = "1")
        @PathVariable(name = "id") Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление роли (таблица division_team_role)")
        @RequestBody DivisionTeamRoleRawDto dto) throws NotFoundException, JsonProcessingException {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("id", id);
        loggerService.createLog(hash, String.format("PUT /api/divisionteamrole/%d", id), getParams, dto);
        dto.setId(id);
        DivisionTeamRoleEntity entity = divisionTeamRoleService.update(dto);
        return DivisionTeamRoleFactory.create(entity);
    }

    @Operation(summary = "Получение информации о ролях в зависимости от переданных параметров. Метод GET будет удаляться, используйте POST /api/divisionteamrole/find", description = "Получение информации о ролях в зависимости от переданных параметров. Метод GET будет удаляться, используйте POST /api/divisionteamrole/find", tags = {"division_team_role"})
    @GetMapping("/api/divisionteamrole/find")
    public List<DivisionTeamRoleContainerDto> find(
        @RequestParam(value = "page", required = false) Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @Parameter(name = "successor_readiness_date_from_plus_year", description = "", example = "2021-06-01")
        @RequestParam(name = "successor_readiness_date_from_plus_year", required = false) Boolean successorReadinessDateFromPlusYear,
        @Parameter(name = "successor_readiness_date_from_start", description = "Дата для division_team_successor_readiness, которая меньше, чем division_team_successor_readiness.date_from", example = "2021-06-01")
        @RequestParam(name = "successor_readiness_date_from_start", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date successorReadinessDateFromStart,
        @Parameter(name = "successor_readiness_date_from_end", description = "Дата для division_team_successor_readiness, которая больше, чем division_team_successor_readiness.date_from", example = "2021-06-01")
        @RequestParam(name = "successor_readiness_date_from_end", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date successorReadinessDateFromEnd,
        @Parameter(name = "assignment_rotation_date_from_start", description = "Дата для division_team_assignment_rotation, которая меньше, чем division_team_assignment_rotation.date_from", example = "2021-06-01")
        @RequestParam(name = "assignment_rotation_date_from_start", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date assignmentRotationDateFromStart,
        @Parameter(name = "assignment_rotation_date_from_end", description = "Дата для division_team_assignment_rotation, которая больше, чем division_team_assignment_rotation.date_from", example = "2021-06-01")
        @RequestParam(name = "assignment_rotation_date_from_end", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date assignmentRotationDateFromEnd,
        @Parameter(name = "successor_date_from", description = "Дата для division_team_successor, котрая равна division_team_successor.date_from", example = "2021-06-01")
        @RequestParam(name = "successor_date_from", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date successorDateFrom,
        @Parameter(name = "division_team_role_id", description = "Массив идентификаторов роли в команде подразделения (таблица division_team_role).", example = "[1, 2, 3]")
        @RequestParam(name = "division_team_role_id", required = false) Long[] divisionTeamRoleId,
        @Parameter(name = "division_team_id", description = "Массив идентификаторов команды (таблица division_team).", example = "[1, 2, 3]")
        @RequestParam(name = "division_team_id", required = false) Long[] divisionTeamId) {
        List<DivisionTeamRoleContainerDto> result = new ArrayList<>();

        if (divisionTeamRoleId != null && divisionTeamId != null) {
            List<Long> divisionTeamRoleIds = new ArrayList<>(Arrays.asList(divisionTeamRoleId));
            List<Long> divisionTeamIds = new ArrayList<>(Arrays.asList(divisionTeamId));
            List<DivisionTeamRoleDto> teamRoles = divisionTeamRoleDao.findAllByIdsAndDivisionTeams(divisionTeamRoleIds, divisionTeamIds, CoreUtils.getPageable(page, size))
                .stream()
                .map(DivisionTeamRoleFactory::create)
                .collect(Collectors.toList());
            teamRoles.forEach(teamRole -> result.add(divisionService.createRoleContainer(teamRole, false)));
            return result;
        }

        if (divisionTeamRoleId != null) {
            List<Long> divisionTeamRoleIds = new ArrayList<>(Arrays.asList(divisionTeamRoleId));
            List<DivisionTeamRoleDto> teamRoles = divisionTeamRoleDao.findByIds(divisionTeamRoleIds, CoreUtils.getPageable(page, size))
                .stream()
                .map(DivisionTeamRoleFactory::create)
                .collect(Collectors.toList());
            teamRoles.forEach(teamRole -> result.add(divisionService.createRoleContainer(teamRole, false)));
            return result;
        }

        if (divisionTeamId != null) {
            List<Long> divisionTeamIds = new ArrayList<>(Arrays.asList(divisionTeamId));
            List<DivisionTeamRoleDto> teamRoles = divisionTeamRoleDao.findAllByDivisionTeams(divisionTeamIds, CoreUtils.getPageable(page, size))
                .stream()
                .map(DivisionTeamRoleFactory::create)
                .collect(Collectors.toList());
            teamRoles.forEach(teamRole -> result.add(divisionService.createRoleContainer(teamRole, false)));
            return result;
        }

        if (successorReadinessDateFromStart != null) {
            List<DivisionTeamRoleDto> teamRoles = divisionTeamRoleDao
                .findAllBySuccessorReadinessDateFromStart(successorReadinessDateFromStart, CoreUtils.getPageable(page, size))
                .stream()
                .map(DivisionTeamRoleFactory::create)
                .collect(Collectors.toList());
            teamRoles.forEach(teamRole -> result.add(divisionService.createRoleContainer(teamRole, false)));
            return result;
        }
        if (successorReadinessDateFromEnd != null) {
            List<DivisionTeamRoleDto> teamRoles = divisionTeamRoleDao
                .findAllBySuccessorReadinessDateFromEnd(successorReadinessDateFromEnd, CoreUtils.getPageable(page, size))
                .stream()
                .map(DivisionTeamRoleFactory::create)
                .collect(Collectors.toList());
            teamRoles.forEach(teamRole -> result.add(divisionService.createRoleContainer(teamRole, false)));
            return result;
        }
        if (assignmentRotationDateFromStart != null) {
            List<DivisionTeamRoleDto> teamRoles = divisionTeamRoleDao
                .findAllByAssignmentRotationDateFromStart(assignmentRotationDateFromStart, CoreUtils.getPageable(page, size))
                .stream()
                .map(DivisionTeamRoleFactory::create)
                .collect(Collectors.toList());
            teamRoles.forEach(teamRole -> result.add(divisionService.createRoleContainer(teamRole, false)));
            return result;
        }
        if (assignmentRotationDateFromEnd != null) {
            List<DivisionTeamRoleDto> teamRoles = divisionTeamRoleDao
                .findAllByAssignmentRotationDateFromEnd(assignmentRotationDateFromEnd, CoreUtils.getPageable(page, size))
                .stream()
                .map(DivisionTeamRoleFactory::create)
                .collect(Collectors.toList());
            teamRoles.forEach(teamRole -> result.add(divisionService.createRoleContainer(teamRole, false)));
            return result;
        }
        if (successorDateFrom != null) {
            List<DivisionTeamRoleDto> teamRoles = divisionTeamRoleDao
                .findAllBySuccessorDateFrom(successorDateFrom, CoreUtils.getPageable(page, size))
                .stream()
                .map(DivisionTeamRoleFactory::create)
                .collect(Collectors.toList());
            teamRoles.forEach(teamRole -> result.add(divisionService.createRoleContainer(teamRole, false)));
            return result;
        }
        if (successorReadinessDateFromPlusYear != null) {
            List<DivisionTeamRoleDto> teamRoles = divisionTeamRoleDao
                .findAllBySuccessorReadinessDateFromPlusOneYear(CoreUtils.getPageable(page, size))
                .stream()
                .map(DivisionTeamRoleFactory::create)
                .collect(Collectors.toList());
            teamRoles.forEach(teamRole -> result.add(divisionService.createRoleContainer(teamRole, false)));
            return result;
        }
        return result;
    }

    @Operation(summary = "Получение информации о ролях в зависимости от переданных параметров", description = "Получение информации о ролях в зависимости от переданных параметров.", tags = {"division_team_role"})
    @PostMapping("/api/divisionteamrole/find")
    public List<DivisionTeamRoleContainerDto> findPost(
        @RequestParam(value = "page", required = false) Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @Parameter(name = "successor_readiness_date_from_plus_year", description = "", example = "2021-06-01")
        @RequestParam(name = "successor_readiness_date_from_plus_year", required = false) Boolean successorReadinessDateFromPlusYear,
        @Parameter(name = "successor_readiness_date_from_start", description = "Дата для division_team_successor_readiness, которая меньше, чем division_team_successor_readiness.date_from", example = "2021-06-01")
        @RequestParam(name = "successor_readiness_date_from_start", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date successorReadinessDateFromStart,
        @Parameter(name = "successor_readiness_date_from_end", description = "Дата для division_team_successor_readiness, которая больше, чем division_team_successor_readiness.date_from", example = "2021-06-01")
        @RequestParam(name = "successor_readiness_date_from_end", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date successorReadinessDateFromEnd,
        @Parameter(name = "assignment_rotation_date_from_start", description = "Дата для division_team_assignment_rotation, которая меньше, чем division_team_assignment_rotation.date_from", example = "2021-06-01")
        @RequestParam(name = "assignment_rotation_date_from_start", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date assignmentRotationDateFromStart,
        @Parameter(name = "assignment_rotation_date_from_end", description = "Дата для division_team_assignment_rotation, которая больше, чем division_team_assignment_rotation.date_from", example = "2021-06-01")
        @RequestParam(name = "assignment_rotation_date_from_end", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date assignmentRotationDateFromEnd,
        @Parameter(name = "successor_date_from", description = "Дата для division_team_successor, котрая равна division_team_successor.date_from", example = "2021-06-01")
        @RequestParam(name = "successor_date_from", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date successorDateFrom,
        @Parameter(name = "division_team_role_id", description = "Массив идентификаторов роли в команде подразделения (таблица division_team_role).", example = "[1, 2, 3]")
        @RequestParam(name = "division_team_role_id", required = false) List<Long> divisionTeamRoleId,
        @Parameter(name = "division_team_id", description = "Массив идентификаторов команды (таблица division_team).", example = "[1, 2, 3]")
        @RequestParam(name = "division_team_id", required = false) List<Long> divisionTeamId,
        @Parameter(name = "division_id", description = "Идентификатор подразделения (таблица division).", example = "123")
        @RequestParam(name = "division_id", required = false) Long divisionId,
        @Parameter(name = "search", description = "Строка поиска по фамилии, имени, отчеству или номеру сотрудника (таблица employee). Работает только с legal_entity, остальные параметры игнорируются.", example = "Ива Иванович")
        @RequestParam(name = "search", required = false) String searchingValue,
        @Parameter(name = "employee_successor", description = "Идентификатор сотрудника (таблица division_team_successor).", example = "5")
        @RequestParam(name = "employee_successor", required = false) Long employeeSuccessorId,
        @Parameter(name = "legal_entity_id", description = "Массив идентификаторов организации (таблица legal_entity).", example = "[1, 2, 3]")
        @RequestParam(name = "legal_entity_id", required = false) List<Long> legalEntityId) throws BadRequestException {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("successor_readiness_date_from_plus_year", successorReadinessDateFromPlusYear);
        getParams.put("successor_readiness_date_from_start", successorReadinessDateFromStart);
        getParams.put("successor_readiness_date_from_end", successorReadinessDateFromEnd);
        getParams.put("assignment_rotation_date_from_start", assignmentRotationDateFromStart);
        getParams.put("assignment_rotation_date_from_end", assignmentRotationDateFromEnd);
        getParams.put("successor_date_from", successorDateFrom);
        getParams.put("division_team_role_id", divisionTeamRoleId);
        getParams.put("division_team_id", divisionTeamId);
        getParams.put("division_id", divisionId);
        getParams.put("search", searchingValue);
        getParams.put("employee_successor", employeeSuccessorId);
        getParams.put("legal_entity_id", legalEntityId);
        loggerService.createLog(hash, "POST /api/divisionteamrole/find", getParams, null);
        boolean isDivisionTeamRoleIdOnlyParam = getParams.values().stream().mapToInt(item -> item != null ? 1 : 0).sum() == 1;
        return divisionTeamRoleService.findDivisionTeamRoles(page, size, successorReadinessDateFromPlusYear, successorReadinessDateFromStart,
            successorReadinessDateFromEnd, assignmentRotationDateFromStart, assignmentRotationDateFromEnd, successorDateFrom,
            divisionTeamRoleId, divisionTeamId, divisionId, searchingValue, employeeSuccessorId, legalEntityId, isDivisionTeamRoleIdOnlyParam);

    }
}
