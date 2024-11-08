package me.goodt.vkpht.module.orgstructure.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.orgstructure.domain.dao.simple.PositionAssignmentSmp;
import me.goodt.vkpht.module.orgstructure.api.dto.*;
import com.goodt.drive.rtcore.dto.rostalent.position.PositionListRequest;
import com.goodt.drive.rtcore.dto.rostalent.position.PositionListResponse;
import com.goodt.drive.rtcore.dto.tasksetting2.FilterAwarePageResponse;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.*;

public interface IPositionService {
    PositionTypeEntity findPositionType(Long id) throws NotFoundException;

    List<PositionTypeEntity> findPositionTypeAll();

    PositionTypeEntity createPositionType(PositionTypeDto dto);

    PositionTypeEntity updatePositionType(PositionTypeDto dto) throws NotFoundException;

    void deletePositionType(Long id) throws NotFoundException;

    PositionKrLevelEntity findPositionKrLevel(Long id) throws NotFoundException;

    List<PositionKrLevelEntity> findPositionKrLevelAll();

    PositionKrLevelEntity createPositionKrLevel(PositionKrLevelDto dto);

    PositionKrLevelEntity updatePositionKrLevel(PositionKrLevelDto dto) throws NotFoundException;

    Long deletePositionKrLevel(Long id) throws NotFoundException;

    PositionPositionKrLevelEntity findPositionPositionKrLevel(Long positionId, Long positionKrLevelId) throws NotFoundException;

    PositionPositionKrLevelEntity createPositionPositionKrLevel(PositionPositionKrLevelDto dto) throws NotFoundException;

    void deletePositionPositionKrLevel(Long positionId, Long positionKrLevelId) throws NotFoundException;

    PositionGroupEntity findPositionGroup(Long id) throws NotFoundException;

    List<PositionGroupEntity> findPositionGroupAll();

    PositionGroupEntity createPositionGroup(PositionGroupDto dto);

    PositionGroupEntity updatePositionGroup(PositionGroupDto dto) throws NotFoundException;

    Long deletePositionGroup(Long id) throws NotFoundException;

    PositionGroupPositionEntity findPositionGroupPosition(Long id) throws NotFoundException;

    List<PositionGroupPositionEntity> findPositionGroupPositionAll(Long positionId, Long positionGroupId);

    PositionGroupPositionEntity createPositionGroupPosition(PositionGroupPositionDto dto) throws NotFoundException;

    void deletePositionGroupPosition(Long id) throws NotFoundException;

    PositionGradeEntity findPositionGrade(Long id) throws NotFoundException;

    List<PositionGradeEntity> findPositionGradeAll();

    PositionGradeEntity createPositionGrade(PositionGradeDto dto);

    PositionGradeEntity updatePositionGrade(PositionGradeDto dto) throws NotFoundException;

    Long deletePositionGrade(Long id) throws NotFoundException;

    PositionPositionGradeEntity findPositionPositionGrade(Long positionId, Long positionGradeId) throws NotFoundException;

    PositionPositionGradeEntity createPositionPositionGrade(PositionPositionGradeDto dto) throws NotFoundException;

    void deletePositionPositionGrade(Long positionId, Long positionGradeId) throws NotFoundException;

    PositionImportanceReasonGroupEntity findPositionImportanceReasonGroup(Long id) throws NotFoundException;

    List<PositionImportanceReasonGroupEntity> findPositionImportanceReasonGroupAll();

    PositionImportanceReasonGroupEntity createPositionImportanceReasonGroup(PositionImportanceReasonGroupDto dto);

    PositionImportanceReasonGroupEntity updatePositionImportanceReasonGroup(PositionImportanceReasonGroupDto dto) throws NotFoundException;

    Long deletePositionImportanceReasonGroup(Long id) throws NotFoundException;

    PositionSuccessorEntity findPositionSuccessor(Long id) throws NotFoundException;

    List<PositionSuccessorEntity> findPositionSuccessorAll();

    PositionSuccessorEntity getPositionSuccessorById(Long id) throws NotFoundException;

    PositionSuccessorEntity createPositionSuccessor(PositionSuccessorRawDto dto) throws NotFoundException;

    PositionSuccessorEntity updatePositionSuccessor(Long id, PositionSuccessorRawDto dto) throws NotFoundException;

    Long deletePositionSuccessor(Long id) throws NotFoundException;

    PositionSuccessorReadinessEntity findPositionSuccessorReadiness(Long id) throws NotFoundException;

    List<PositionSuccessorReadinessEntity> findPositionSuccessorReadinessAll();

    PositionSuccessorReadinessEntity getPositionSuccessorReadinessById(Long id) throws NotFoundException;

    PositionSuccessorReadinessEntity createPositionSuccessorReadiness(PositionSuccessorReadinessRawDto dto) throws NotFoundException;

    PositionSuccessorReadinessEntity updatePositionSuccessorReadiness(PositionSuccessorReadinessRawDto dto) throws NotFoundException;

    Long deletePositionSuccessorReadiness(Long id) throws NotFoundException;

    List<PositionCategoryEntity> findPositionCategoryAll();

    List<PositionRankEntity> findPositionRankAll();

    PositionEntity getPosition(Long positionId) throws NotFoundException;

    PositionEntity updatePosition(Long positionId, PositionInputDto dto) throws NotFoundException;

    List<PositionEntity> getPositionByEmployeeIdAndDivisionIds(Long employeeId, List<Long> divisionIds);

    List<PositionAssignmentSmp> getAllByDivision(Long divisionTeamId, boolean withChildren);

    PositionAssignmentEntity getPositionAssignmentByPositionId(Long positionId);

    Map<Long, List<PositionAssignmentEntity>> findActualPositionAssignmentByEmployeeIds(Collection<Long> employeeIds);

    List<PositionImportanceEntity> findPositionImportanceAll();

    PositionImportanceEntity getPositionImportance(Integer id) throws NotFoundException;

    PositionPositionImportanceEntity getPositionPositionImportance(Long id) throws NotFoundException;

    List<PositionPositionImportanceEntity> getAllPositionPositionImportance(boolean withClosed, List<Long> divisionTeamIds, List<Long> userIds, List<Long> positionIds);

    PositionPositionImportanceEntity createPositionPositionImportance(PositionPositionImportanceInputDto dto, String externalId) throws NotFoundException;

    PositionPositionImportanceEntity updatePositionPositionImportance(Long id, PositionPositionImportanceInputDto dto, String externalId) throws NotFoundException;

    Long deletePositionPositionImportance(Long id) throws NotFoundException;

    FilterAwarePageResponse<PositionListResponse> bffPositionList(PositionListRequest request);
}
