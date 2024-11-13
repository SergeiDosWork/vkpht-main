package me.goodt.vkpht.module.orgstructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.auth.sur.service.SurProtectedAttr;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.common.api.dto.OperationResult;
import me.goodt.vkpht.module.orgstructure.api.dto.RoleInfo;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamAssignmentDto;
import me.goodt.vkpht.common.api.exception.BadRequestException;
import me.goodt.vkpht.common.api.exception.ForbiddenException;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.api.LoggerService;
import me.goodt.vkpht.module.orgstructure.api.RoleService;

@RestController
@GeneralAPIResponses
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final LoggerService loggerService;

    @Operation(summary = "Получение информации о всех актуальных ролях", description = "Получение информации о всех актуальных ролях", tags = {"role"})
    @GetMapping("/api/role/list")
    public List<RoleInfo> getRoleList() {
        return roleService.getActualAssignableRoleList();
    }

    @Operation(summary = "Получение массива информации о назначениях в организацию", description = "Получение массива информации о назначениях в организацию", tags = {"role"})
    @GetMapping("/api/role/legalentityassignments")
    public List<LegalEntityTeamAssignmentDto> getLegalEntityAssignments(
        @Parameter(name = "legal_entity_id", description = "Массив идентификаторов организации (таблица legal_entity).", example = "[1,2,3]")
        @RequestParam(name = "legal_entity_id", required = false) List<Long> legalEntityIds,
        @Parameter(name = "role_id", description = "Массив идентификаторов роли (таблица role).", example = "[1,2,3]")
        @RequestParam(name = "role_id", required = false) List<Long> roleIds,
        @Parameter(name = "employee_id", description = "Массив идентификаторов сотрудника (таблица employee)", example = "[1,2,3]")
        @RequestParam(name = "employee_id", required = false) List<Long> employeeIds) {
        return roleService.getLegalTeamAssignmentInfo(legalEntityIds, roleIds, employeeIds);
    }

    @Operation(summary = "Присвоение роли сотруднику в организации", description = "Присвоение роли сотруднику в организации", tags = {"role"})
    @PostMapping("/api/role/setemployeerole")
    @SurProtected(operation = SurOperation.EXEC_ATTR)
    public OperationResult setEmployeeRole(
        @Parameter(name = "legal_entity_id", description = "Идентификатор организации (таблица legal_entity).", example = "1")
        @RequestParam(name = "legal_entity_id") @SurProtectedAttr(attributeName = "legal_entity_id") Long legalEntityId,
        @Parameter(name = "role_id", description = "Идентификатор роли (таблица role).", example = "1")
        @RequestParam(name = "role_id") Long roleId,
        @Parameter(name = "employee_id", description = "Идентификатор сотрудника (таблица employee).", example = "1")
        @RequestParam(name = "employee_id") Long employeeId) throws JsonProcessingException, NotFoundException {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("legal_entity_id", legalEntityId);
        getParams.put("role_id", roleId);
        getParams.put("employee_id", employeeId);
        loggerService.createLog(hash, "POST /api/role/setemployeerole", getParams, null);
        try {
            roleService.setEmployeeRole(legalEntityId, roleId, employeeId);
            return new OperationResult(true, "");
        } catch (ForbiddenException e) {
            return new OperationResult(false, e.getMessage());
        }
    }

    @Operation(summary = "Снятие сотрудника с роли", description = "Закрывает назначение сотрудника в команду по указанным параметрам", tags = {"role"})
    @PostMapping("/api/role/clearemployeerole")
    public void clearEmployeeRole(
        @Parameter(name = "legal_entity_id", description = "Идентификатор организации (таблица legal_entity_team_assignment).", example = "1")
        @RequestParam(name = "legal_entity_id", required = false) Long legalEntityId,
        @Parameter(name = "role_id", description = "Идентификатор роли (таблица legal_entity_team_assignment).", example = "1")
        @RequestParam(name = "role_id", required = false) Long roleId,
        @Parameter(name = "employee_id", description = "Идентификатор сотрудника (таблица legal_entity_team_assignment).", example = "1")
        @RequestParam(name = "employee_id", required = false) Long employeeId) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("legal_entity_id", legalEntityId);
        getParams.put("role_id", roleId);
        getParams.put("employee_id", employeeId);
        loggerService.createLog(hash, "POST /api/role/clearemployeerole", getParams, null);
        roleService.clearEmployeeRole(legalEntityId, roleId, employeeId);
    }

    @Operation(summary = "Получение информации о назначениях HR в организацию", description = "Получение информации о назначениях HR в организацию", tags = {"role"})
    @GetMapping("/api/role/hrlegalentityassignments")
    public List<LegalEntityTeamAssignmentDto> getHrLegalEntityTeamAssignment(
        @Parameter(name = "division_team_assignment_id", description = "Идентификатор назначения в команде (таблица division_team_assignment). В метод передаём один из параметров", example = "3")
        @RequestParam(name = "division_team_assignment_id", required = false) Long assignmentId,
        @Parameter(name = "employee_id", description = "Идентификатор сотрудника (таблица employee). В метод передаём один из параметров", example = "20")
        @RequestParam(name = "employee_id", required = false) Long employeeId) throws BadRequestException {
        return roleService.getHrLegalEntityTeamAssignment(assignmentId, employeeId);
    }
}
