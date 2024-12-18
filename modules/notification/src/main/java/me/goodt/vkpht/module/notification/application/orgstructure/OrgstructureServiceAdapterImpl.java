package me.goodt.vkpht.module.notification.application.orgstructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.module.orgstructure.api.AssignmentService;
import me.goodt.vkpht.module.orgstructure.api.DivisionService;
import me.goodt.vkpht.module.orgstructure.api.DivisionTeamRoleService;
import me.goodt.vkpht.module.orgstructure.api.EmployeeService;
import me.goodt.vkpht.module.orgstructure.api.LegalEntityTeamAssignmentService;
import me.goodt.vkpht.module.orgstructure.api.LegalService;
import me.goodt.vkpht.module.orgstructure.api.PositionService;
import me.goodt.vkpht.module.orgstructure.api.RoleService;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoRequestDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleContainerDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoResponse;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityDto;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorReadinessDto;
import me.goodt.vkpht.module.orgstructure.api.dto.request.FindDivisionTeamRolesRequest;
import me.goodt.vkpht.module.orgstructure.api.dto.request.FindEmployeeRequest;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrgstructureServiceAdapterImpl implements OrgstructureServiceAdapter {

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
        FindDivisionTeamRolesRequest request = new FindDivisionTeamRolesRequest();
        request.setDivisionTeamRoleIdOnlyParam(isDivisionTeamRoleIdOnlyParam);
        request.setDivisionTeamRoleIds(divisionTeamRoleIds);
        request.setSuccessorReadinessDateFromStart(successorReadinessDateFromStart);
        request.setSuccessorReadinessDateFromEnd(successorReadinessDateFromEnd);
        request.setAssignmentRotationDateFromStart(assignmentRotationDateFromStart);
        request.setAssignmentRotationDateFromEnd(assignmentRotationDateFromEnd);
        request.setSuccessorDateFrom(successorDateFrom);
        return divisionTeamRoleService.findDivisionTeamRoles(request);
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
        FindEmployeeRequest request = new FindEmployeeRequest();
        request.setEmployeeIds(employeeIds);
        return employeeService.findEmployees(request);
    }

    @Override
    public EmployeeInfoResponse findEmployeeByEmails(List<String> emails) {
        FindEmployeeRequest request = new FindEmployeeRequest();
        request.setEmails(emails);
        return employeeService.findEmployees(request);
    }

    @Override
    public EmployeeInfoResponse findEmployeeByDivision(List<Long> divisionIds) {
        FindEmployeeRequest request = new FindEmployeeRequest();
        request.setDivisionIds(divisionIds);
        return employeeService.findEmployees(request);
    }

    @Override
    public List<LegalEntityDto> getLegalEntityList(List<Long> divisionIds, List<Long> divisionGroupIds) {
        return legalService.getLegalEntityList(divisionIds, divisionGroupIds);
    }

    @Override
    public List<DivisionInfoDto> getDivisionList(DivisionInfoRequestDto dto) {
        return divisionService.getDivisionInfoByParams(dto.getDivisionIds(), dto.getParentId(), dto.getLegalEntityId(), dto.getGroupIds(), dto.isWithChilds());
    }
}
