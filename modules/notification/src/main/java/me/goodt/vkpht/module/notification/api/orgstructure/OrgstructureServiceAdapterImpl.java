package me.goodt.vkpht.module.notification.api.orgstructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.module.orgstructure.api.AssignmentService;
import me.goodt.vkpht.module.orgstructure.api.DivisionService;
import me.goodt.vkpht.module.orgstructure.api.DivisionTeamRoleService;
import me.goodt.vkpht.module.orgstructure.api.EmployeeService;

import me.goodt.vkpht.module.orgstructure.api.LegalEntityTeamAssignmentService;
import me.goodt.vkpht.module.orgstructure.api.LegalService;
import me.goodt.vkpht.module.orgstructure.api.PositionService;
import me.goodt.vkpht.module.orgstructure.api.RoleService;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentShortDto;

import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.goodt.drive.notify.application.dto.ResponseNumberDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoRequestDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleContainerDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoResponse;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeMetaLukViewDto;
import me.goodt.vkpht.module.orgstructure.api.dto.FunctionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.IdDto;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityDto;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorReadinessDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RequestNumbersDto;
import com.goodt.drive.notify.application.services.clients.RtCoreApi;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrgstructureServiceAdapterImpl implements OrgstructureServiceAdapter {

    private static final String EMPLOYEE = "employee";
    private static final String TEAM = "team";

    private final RtCoreApi rtCoreApi;
    private final EmployeeService employeeService;
    private final AssignmentService assignmentService;
    private final DivisionService divisionService;
    private final DivisionTeamRoleService divisionTeamRoleService;
    private final RoleService roleService;
    private final LegalService legalService;
    private final LegalEntityTeamAssignmentService legalEntityTeamAssignmentService;
    private final PositionService positionService;
    private final AuthService authService;

    @Override
    public EmployeeInfoDto getEmployeeInfo(Long employeeId) {
        return employeeService.getEmployeeInfo(employeeId);
    }

    public EmployeeInfoDto getEmployeeInfoByAssignment(Long assignmentId) {
        return employeeService.getEmployeeInfoByAssignment(assignmentId);
    }

    public List<DivisionTeamAssignmentDto> getAssignments() {
        return assignmentService.getDivisionTeamAssignmentsFull(null, null, null, null, null);
    }

    @Override
    public List<DivisionTeamAssignmentDto> getAssigmentByEmployeeIds(List<Long> employeeIds) {
        return assignmentService.getDivisionTeamAssignmentsFull(null, employeeIds, null, null, null);
    }

    @Override
    public List<DivisionTeamAssignmentDto> getAssignments(List<Long> userIds, List<Long> employeeIds) {
        return assignmentService.getDivisionTeamAssignmentsFull(userIds, employeeIds, null, null, null);
    }

    @Override
    public List<DivisionTeamAssignmentDto> getAssignmentsWithDto(List<Long> userIds) {
        return assignmentService.getDivisionTeamAssignmentsFull(userIds, null, null, null, null);
    }

    @Override
    public DivisionTeamAssignmentDto getEmployeeHead(Long employeeId, Long divisionTeamId) {
        return assignmentService.getTeamDivisionHead(employeeId, null, divisionTeamId, null);
    }

    @Override
    public DivisionInfoDto getDivisionInfo(Long id) {
        return divisionService.getDivisionInfoDto(id);
    }

    @Override
    public List<LegalEntityTeamAssignmentDto> getLegalEntityAssignments(List<Long> legalEntityIds, List<Long> roleIds, List<Long> employeeIds) {
        return roleService.getLegalTeamAssignmentInfo(legalEntityIds, roleIds, employeeIds);
    }

    @Override
    public List<LegalEntityTeamAssignmentDto> getTeamLegalEntityAssignments(Long legalEntityTeamAssignmentId, Long employeeId,
                                                                            String externalEmployeeId, Long legalEntityTeamId) {
        return legalEntityTeamAssignmentService.getLegalEntityTeamAssignments(legalEntityTeamAssignmentId, employeeId, externalEmployeeId, legalEntityTeamId);
    }

    @Override
    public List<DivisionTeamRoleContainerDto> findDivisionTeamRoles(Date successorReadinessDateFromStart,
                                                                    Date successorReadinessDateFromEnd,
                                                                    Date assignmentRotationDateFromStart,
                                                                    Date assignmentRotationDateFromEnd,
                                                                    Date successorDateFrom,
                                                                    Long divisionTeamRoleId) {
        boolean isDivisionTeamRoleIdOnlyParam = divisionTeamRoleId != null;
        List<Long> divisionTeamRoleIds = isDivisionTeamRoleIdOnlyParam ? List.of(divisionTeamRoleId) : null;
        return divisionTeamRoleService.findDivisionTeamRoles(null, null, null, successorReadinessDateFromStart,
            successorReadinessDateFromEnd, assignmentRotationDateFromStart, assignmentRotationDateFromEnd, successorDateFrom,
            divisionTeamRoleIds, null, null, null, null, null, isDivisionTeamRoleIdOnlyParam);
    }

    @Override
    public Long getEmployeeId() {
        return authService.getUserEmployeeId();
    }

    @Override
    public List<DivisionTeamAssignmentShortDto> getTeamDivisionSubordinates(Long divisionTeamId) {
        return assignmentService.getDivisionTeamSubordinates(null, null, divisionTeamId, false);
    }

    @Override
    public DivisionTeamSuccessorDto getDivisionTeamSuccessor(Long id) {
        return divisionService.getDivisionTeamSuccessor(id);
    }

    @Override
    public List<PositionDto> getPositionByEmployeeIdAndDivisionId(Long employeeId, Long divisionId) {
        List<Long> divisionIds = divisionId != null ? List.of(divisionId) : null;
        return positionService.getPositionByEmployeeIdAndDivisionIds(employeeId, divisionIds);
    }

    @Override
    public Boolean checkEmployeeHeadTeamByAssignment(Long divisionTeamAssignmentId) {
        return divisionService.checkEmployeeHeadTeamByAssignment(divisionTeamAssignmentId);
    }

    @Override
    public DivisionTeamAssignmentDto getTeamDivisionHeadHead(Long employeeId, Long divisionTeamId) {
        return divisionService.getTeamDivisionHeadHead(employeeId, null, divisionTeamId);
    }

    @Override
    public List<Long> getDivisionTeamAssignmentIdsInLegalEntityIdsExceptAssignmentId(Long assignmentId, List<Long> legalEntityIds) {
        long employeeId = authService.getUserEmployeeId();
        return divisionService.findAllInLegalEntityIdsExceptAssignmentId(assignmentId, legalEntityIds, employeeId);
    }

    @Override
    public PositionDto getPosition(Long positionId) {
        return positionService.getPosition(positionId);
    }

    @Override
    public PositionAssignmentDto getPositionAssignmentByPositionId(Long positionId) {
        return positionService.getPositionAssignmentByPositionId(positionId);
    }

    @Override
    public List<DivisionTeamAssignmentDto> getTeamDivisionAssignmentsByLegalEntity(List<Long> legalEntityIds, boolean isHead) {
        return assignmentService.getTeamDivisionAssignmentsByLegalEntity(legalEntityIds, isHead, true, true, true, true);
    }

    @Override
    public PositionSuccessorDto getPositionSuccessor(Long positionSuccessorId) {
        return positionService.getPositionSuccessorById(positionSuccessorId);
    }

    @Override
    public PositionSuccessorReadinessDto getPositionSuccessorReadiness(Long positionSuccessorReadinessId) {
        return positionService.getPositionSuccessorReadinessById(positionSuccessorReadinessId);
    }

    @Override
    public List<PositionDto> getPositionAssignmentByEmployeeIdAndDivisionId(Long employeeId, Long divisionId) {
        List<Long> divisionIds = divisionId != null ? List.of(divisionId) : null;
        return positionService.getPositionByEmployeeIdAndDivisionIds(employeeId, divisionIds);
    }

    @Override
    public LegalEntityDto getLegalEntity(Long legalEntityId) {
        return legalService.getLegalEntityDto(legalEntityId);
    }

    @Override
    public EmployeeInfoResponse findEmployee(List<Long> employeeIds) {
        return rtCoreApi.findEmployee(Map.of("id", employeeIds, "size", Integer.MAX_VALUE));
    }

    @Override
    public EmployeeInfoResponse findEmployeeByEmails(List<String> emails) {
        return rtCoreApi.findEmployee(Map.of("emails", emails));
    }

    @Override
    public EmployeeInfoResponse findEmployeeByDivision(List<Long> divisionIds) {
        return rtCoreApi.findEmployee(Map.of("division", divisionIds, "size", Integer.MAX_VALUE));
    }

    @Override
    public List<LegalEntityDto> getLegalEntityList(List<Long> divisionIds, List<Long> divisionGroupIds) {
        return rtCoreApi.getLegalEntityList(
            Stream.of(new AbstractMap.SimpleEntry<String, Object>("division_id", divisionIds),
                    new AbstractMap.SimpleEntry<String, Object>("division_group_id", divisionGroupIds))
                .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll)
        );
    }

    @Override
    public List<EmployeeMetaLukViewDto> getEmployeeMetaLukView() {
        return rtCoreApi.getEmployeeMetaLukView();
    }

    @Override
    public List<DivisionInfoDto> getDivisionList(DivisionInfoRequestDto divisionInfoRequestDto) {
        return rtCoreApi.divisionInfos(divisionInfoRequestDto);
    }

    @Override
    public ResponseNumberDto checkNumbersFromFile(RequestNumbersDto dto) {
        return rtCoreApi.checkNumbersFromFile(dto);
    }

    @Override
    public List<FunctionDto> getFunctionList() {
        return rtCoreApi.getFunctionList();
    }
}
