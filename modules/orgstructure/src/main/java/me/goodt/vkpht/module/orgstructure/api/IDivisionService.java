package me.goodt.vkpht.module.orgstructure.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.Date;
import java.util.List;

import com.goodt.drive.rtcore.data.DivisionInfo;
import com.goodt.drive.rtcore.data.DivisionPathData;
import com.goodt.drive.rtcore.data.DivisionShortInfo;
import com.goodt.drive.rtcore.data.PositionInfo;
import me.goodt.vkpht.module.orgstructure.api.dto.*;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionGroupEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamSuccessorEntity;

public interface IDivisionService {
    DivisionEntity getDivision(Long id) throws NotFoundException;

    List<PositionInfo> getDivisionPositions(Long divisionId);

    List<PositionExtendedDto> getExtendedPositions(List<Long> divisionId, List<Long> positionIds, boolean hasPositionAssignment, Long legalEntityId);

    DivisionTeamSuccessorDto createDivisionTeamSuccessor(Long divisionTeamRoleId, Long employeeId, Long employeeHrId, Date datePriority,
                                                         Integer reasonIdInclusion, String commentInclusion,
                                                         String documentUrlInclusion) throws NotFoundException;

    Boolean deleteDivisionTeamSuccessor(Long divisionTeamSuccessorId, Long employeeHrId, Integer reasonIdExclusion, String commentExclusion, String documentUrlExclusion) throws NotFoundException, AccessDeniedException;

    DivisionTeamSuccessorReadinessDto createDivisionTeamSuccessorReadiness(Long divisionTeamSuccessorId, Integer assignmentReadinessId, Long employeeHrId) throws NotFoundException, AccessDeniedException;

    void deletedDivisionTeamSuccessors() throws NotFoundException;

    void commitTeamDivisionAssignmentRotation(Long divisionTeamAssignmentRotationId, Long employeeId) throws NotFoundException, AccessDeniedException;

    DivisionTeamAssignmentRotationDto createDivisionTeamAssignmentRotation(Long divisionTeamAssignmentId, Integer assignmentRotationId, Long employeeHrId, Boolean techUser) throws NotFoundException, AccessDeniedException;

    Boolean checkEmployeeHeadTeamByAssignment(Long divisionTeamAssignmentId);

    void teamDivisionAssignmentRotationWithDraw(Long divisionTeamAssignmentRotationId, Long employeeId) throws NotFoundException, AccessDeniedException;

    DivisionTeamRoleContainerDto createRoleContainer(DivisionTeamRoleDto divisionTeamRole, boolean withPositionSuccessors);

    List<DivisionTeamRoleContainerDto> createRoleContainers(List<DivisionTeamRoleDto> divisionTeamRoles, boolean withPositionSuccessors);

    List<DivisionWithDivisionTeamsStatDto> teamAssignmentRotationStatDto(Long legalEntityId);

    List<DivisionWithDivisionTeamsWithDivisionTeamRolesAndDivisionTeamSuccessorsDto> getDivisionTeamSuccessorStat(Long legalEntityId);

    void divisionTeamSuccessorUpdateHr(Long divisionTeamSuccessorId, Date dateCommitHr) throws NotFoundException;

    DivisionTeamSuccessorEntity getDivisionTeamSuccessor(Long id) throws NotFoundException;

    Page<DivisionTeamAssignmentEntity> findAllDivisionTeamAssignment(DivisionTeamAssignmentFindRequestDto request, Pageable pageable);

    List<DivisionTeamAssignmentEntity> findAllDivisionTeamAssignment(DivisionTeamAssignmentFindRequestDto request);

    List<Long> findAllDivisionTeamAssignmentIds(DivisionTeamAssignmentFindRequestDto request);

    List<Long> findAllInLegalEntityIdsExceptAssignmentId(Long assignmentId, List<Long> legalEntityIds, Long employeeId);

    List<DivisionGroupEntity> getDivisionGroupEntitiesByIds(List<Long> ids);

    List<DivisionGroupEntity> getAllDivisionGroupEntities();

    List<DivisionInfoDto> getDivisionInfoByParams(List<Long> divisionIds, Long parentId, Long legalEntityId, List<Long> groupIds, boolean withChilds);

    DivisionInfo getDivisionInfo(Long divisionId);

    DivisionInfoDto getDivisionInfoDto(Long divisionId);

    DivisionPathData getDivisionPathInfo(Long divisionId);

    List<DivisionShortInfo> getPath(Long id);

    boolean checkEmployeeHeadTeamDivision(Long employeeId, Long teamId);

    boolean checkEmployeeHeadTeamDivisionSuccessor(Long employeeHeadId, Long employeeId, Long teamId) throws NotFoundException;

    boolean checkEmployeeHr(Long employeeId, Long divisionId) throws NotFoundException;

    boolean checkEmployeeHrSuccessor(Long employeeHrId, Long employeeId, Long teamId);

    void rebuildDivisionTree();

    DivisionTeamEntity getDivisionTeamByTypeAndEmployeeId(Integer divisionTeamTypeId, Long employeeId);

    DivisionTeamDto findDivisionTeamById(Long id);

    List<DivisionTeamDto> findActual();

    List<DivisionDto> findPost(DivisionFindDto dto);
}
