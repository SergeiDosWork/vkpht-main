package me.goodt.vkpht.module.orgstructure.api;

import java.util.Collection;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoRequestDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.FunctionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityDto;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionDto;

public interface OrgstructureService {

    List<DivisionTeamAssignmentDto> getAssignments(Collection<Long> userIds, Collection<Long> employeeIds);

    List<DivisionTeamAssignmentDto> getAssignments();

    List<LegalEntityTeamAssignmentDto> getLegalEntityAssignments(List<Long> legalEntityIds, List<Long> roleIds, List<Long> employeeIds);

    List<LegalEntityTeamAssignmentDto> getTeamLegalEntityAssignments(Long legalEntityTeamAssignmentId,
                                                                     Long employeeId,
                                                                     String externalEmployeeId,
                                                                     Long legalEntityTeamId);

    Boolean checkEmployeeHeadTeamByAssignment(Long divisionTeamAssignmentId);

    List<Long> getDivisionTeamAssignmentIdsInLegalEntityIdsExceptAssignmentId(Long assignmentId, List<Long> legalEntityIds);

    PositionDto getPosition(Long positionId);

    List<LegalEntityDto> getLegalEntityList(List<Long> divisionIds, List<Long> divisionGroupIds);

    List<DivisionInfoDto> getDivisionList(DivisionInfoRequestDto dto);

    List<FunctionDto> getFunctionList();
}
