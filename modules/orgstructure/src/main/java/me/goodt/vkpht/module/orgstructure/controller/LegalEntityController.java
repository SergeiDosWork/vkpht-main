package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityDto;
import me.goodt.vkpht.module.orgstructure.api.dto.projection.LegalEntityTeamAssignmentCompactProjection;
import me.goodt.vkpht.module.orgstructure.api.LegalService;

@RestController
@RequiredArgsConstructor
@GeneralAPIResponses
public class LegalEntityController {

    private final LegalService legalService;

    @Operation(summary = "Получение информации о всех организациях", description = "Получение информации о всех организациях", tags = {"legal_entity"})
    @GetMapping("/api/legalentity/list")
    public List<LegalEntityDto> getLegalEntityList(
            @Parameter(name = "division_id", description = "Массив идентификаторов подразделения (см. таблицу division)")
            @RequestParam(name = "division_id", required = false) List<Long> divisionIds,
            @Parameter(name = "division_group_id", description = "Массив идентификаторов групп подразделений (см. таблицу division)")
            @RequestParam(name = "division_group_id", required = false) List<Long> divisionGroupIds) {
        return legalService.getLegalEntityList(divisionIds, divisionGroupIds);
    }

    @Operation(summary = "Получение информации об организации", description = "Получение информации об организации", tags = {"legal_entity"})
    @GetMapping("/api/legalentity/info")
    public LegalEntityDto getLegalEntity(
            @Parameter(name = "id", description = "Идентификатор организации (таблица legal_entity).", example = "1")
            @RequestParam("id") Long id) {
        return legalService.getLegalEntityDto(id);
    }

    @Hidden
    @Operation(summary = "Получение массива legal_entity_team_id по каждому назначению", description = "Получение массива legal_entity_team_id по каждому назначению", tags = {"legal_entity_team"})
    @GetMapping("/api/legal-entity-team/list-id")
    public Map<Long, List<Long>> getLegalEntityTeamIdList(
            @Parameter(name = "divisionTeamAssignmentId", description = "Массив ИД назначений")
            @RequestParam(name = "divisionTeamAssignmentId") Collection<Long> divisionTeamAssignmentIds) {
        return legalService.getLegalEntityTeamIdList(divisionTeamAssignmentIds);
    }

    @Hidden
    @Operation(summary = "Получение списка LegalEntityTeamAssignmentCompactProjection", description = "Получение списка LegalEntityTeamAssignmentCompactProjection", tags = {"legal_entity_team"})
    @GetMapping("/api/legal-entity-team-assignment-compact/list-by-employee-id")
    public List<LegalEntityTeamAssignmentCompactProjection> getLegalEntityTeamAssignmentCompactByEmployeeId(
            @Parameter(name = "employeeId", description = "ИД сотрудника")
            @RequestParam(name = "employeeId") Long employeeId) {
        return legalService.getLegalEntityTeamAssignmentCompactByEmployeeId(employeeId);
    }
}
