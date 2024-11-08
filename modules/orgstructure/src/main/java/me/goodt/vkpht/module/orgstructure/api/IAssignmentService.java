package me.goodt.vkpht.module.orgstructure.api;

import java.util.Collection;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.*;
import me.goodt.vkpht.module.orgstructure.api.dto.projection.DivisionTeamAssignmentCompactProjection;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentEntity;

public interface IAssignmentService {
    List<DivisionTeamAssignmentEntity> getTeamAssignments(Long employeeId);

    DivisionTeamAssignmentEntity getOneTeamAssignment(Long employeeId, Long divisionTeamId) throws NotFoundException;

    List<DivisionTeamAssignmentEntity> getHeadAssignment(Long divisionTeamId);

    DivisionTeamAssignmentEntity getDivisionTeamAssignment(Long id);

    List<DivisionTeamAssignmentShortDto> getAssignmentsCreateShortWithJobInfo(List<DivisionTeamAssignmentEntity> assignments, boolean withAssignments, boolean withRotations);

    List<DivisionTeamAssignmentDto> getTeamDivisionTeamAssignments(Long divisionTeamId);

    List<DivisionTeamAssignmentDto> getDivisionTeamAssignmentsFull(List<Long> ids, List<Long> employeeIds, String externalId, Long divisionTeamId, Boolean withClosed);

    List<DivisionTeamAssignmentDto> getDivisionTeamAssignments(List<Long> ids, List<Long> employeeIds, Long divisionTeamId, Boolean withClosed);

    List<DivisionTeamAssignmentCompactProjection> getDivisionTeamAssignmentCompactList(Collection<Long> ids, Collection<Long> employeeIds);

    List<DivisionTeamAssignmentCompactProjection> getDivisionTeamAssignmentCompactListInner(Collection<Long> ids, Collection<Long> employeeIds);

    DivisionTeamAssignmentDto getFirstDivisionTeamAssignment();

    CompactAssignmentDto getFirstDivTeamAss();

    List<DivisionTeamAssignmentShortDto> getDivisionTeamSubordinates(Long employeeId, String externalEmployeeId, Long divisionTeamId, boolean withChilds);

    List<DivisionTeamAssignmentShortDto> getDivisionTeamSubordinatesTeam(Long divisionTeamId, Boolean withChilds);

    List<DivisionTeamAssignmentDto> getTeamDivisionAssignmentsByLegalEntity(List<Long> legalEntityIds, boolean isHead, boolean withAssignments, boolean withRotation, boolean withEmployee, boolean withDtr);

    DivisionTeamAssignmentDto getTeamDivisionHead(Long employeeId, String externalId, Long divisionTeamId, Long headLevel);

    Long getEmployeeTaskRole(Long userId, Long employeeIdTaskOpener);

    List<DivisionTeamAssignmentDto> getAssignmentByPositionCategory(Long positionCategoryId);

    List<DivisionTeamAssignmentWithDivisionTeamFullDto> findDivisionTeamAssignmentWithDivisionTeamFull(Collection<Long> ids);

    Boolean checkSubstitute(Long divisionTeamId, Long sessionEmployee);

    List<Long> getDivisionTeamSubordinatesByIds(List<Long> employeeIds);
    List<Integer> getDivisionTeamSubordinatesEmployeeShort(Long employeeId, Boolean withChilds);
     List<EmployeeInfoShortDto> getDivisionTeamSubordinatesEmployeeFull(Long employeeId, Boolean withChilds);

}
