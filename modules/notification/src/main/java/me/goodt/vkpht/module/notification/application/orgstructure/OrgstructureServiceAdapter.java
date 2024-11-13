package me.goodt.vkpht.module.notification.application.orgstructure;

import java.util.Date;
import java.util.List;

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

public interface OrgstructureServiceAdapter {

    EmployeeInfoDto getEmployeeInfo(Long employeeId);

    EmployeeInfoDto getEmployeeInfoByAssignment(Long assignmentId);

    List<DivisionTeamAssignmentDto> getAssignments(List<Long> userIds, List<Long> employeeIds);

    List<DivisionTeamAssignmentDto> getAssignments();

    List<DivisionTeamAssignmentDto> getAssignmentsWithDto(List<Long> userIds);

    List<DivisionTeamAssignmentDto> getAssigmentByEmployeeIds(List<Long> employeeIds);

    DivisionTeamAssignmentDto getEmployeeHead(Long employeeId, Long divisionTeamId);

    DivisionInfoDto getDivisionInfo(Long id);

    List<LegalEntityTeamAssignmentDto> getLegalEntityAssignments(List<Long> legalEntityIds, List<Long> roleIds, List<Long> employeeIds);

    List<LegalEntityTeamAssignmentDto> getTeamLegalEntityAssignments(Long legalEntityTeamAssignmentId,
                                                                     Long employeeId,
                                                                     String externalEmployeeId,
                                                                     Long legalEntityTeamId);

    List<DivisionTeamRoleContainerDto> findDivisionTeamRoles(Date successorReadinessDateFromStart,
                                                             Date successorReadinessDateFromEnd,
                                                             Date assignmentRotationDateFromStart,
                                                             Date assignmentRotationDateFromEnd,
                                                             Date successorDateFrom,
                                                             Long divisionTeamRoleId);

    Long getEmployeeId();

    List<DivisionTeamAssignmentShortDto> getTeamDivisionSubordinates(Long divisionTeamId);

    DivisionTeamSuccessorDto getDivisionTeamSuccessor(Long id);

    List<PositionDto> getPositionByEmployeeIdAndDivisionId(Long employeeId, Long divisionId);

    Boolean checkEmployeeHeadTeamByAssignment(Long divisionTeamAssignmentId);

    DivisionTeamAssignmentDto getTeamDivisionHeadHead(Long employeeId, Long divisionTeamId);

    List<Long> getDivisionTeamAssignmentIdsInLegalEntityIdsExceptAssignmentId(Long assignmentId, List<Long> legalEntityIds);

    PositionDto getPosition(Long positionId);

    PositionAssignmentDto getPositionAssignmentByPositionId(Long positionId);

    List<DivisionTeamAssignmentDto> getTeamDivisionAssignmentsByLegalEntity(List<Long> legalEntityIds, boolean isHead);

    PositionSuccessorDto getPositionSuccessor(Long positionSuccessorId);

    PositionSuccessorReadinessDto getPositionSuccessorReadiness(Long positionSuccessorReadinessId);

    List<PositionDto> getPositionAssignmentByEmployeeIdAndDivisionId(Long employeeId, Long divisionId);

    LegalEntityDto getLegalEntity(Long legalEntityId);

    EmployeeInfoResponse findEmployee(List<Long> employeeIds);

    EmployeeInfoResponse findEmployeeByEmails(List<String> emails);

    EmployeeInfoResponse findEmployeeByDivision(List<Long> divisionIds);

    List<LegalEntityDto> getLegalEntityList(List<Long> divisionIds, List<Long> divisionGroupIds);

    List<DivisionInfoDto> getDivisionList(DivisionInfoRequestDto divisionInfoRequestDto);
}
