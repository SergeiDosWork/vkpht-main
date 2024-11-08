package me.goodt.vkpht.module.orgstructure.api;

import java.util.Collection;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.*;
import com.goodt.drive.rtcore.dto.rostalent.orgstructure.DivisionInfoRequestDto;

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

    List<IEmployeeMetaLukView> getEmployeeMetaLukView();

    List<DivisionInfoDto> getDivisionList(DivisionInfoRequestDto dto);

    List<FunctionDto> getFunctionList();
}
