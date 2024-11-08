package me.goodt.vkpht.module.orgstructure.application;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import com.goodt.drive.rtcore.constants.ComponentFieldCode;
import me.goodt.vkpht.common.domain.dao.NativeDao;
import me.goodt.vkpht.common.domain.dao.tasksetting2.TaskFieldDao;
import com.goodt.drive.rtcore.dto.DivisionTeamTree;
import com.goodt.drive.rtcore.dto.rostalent.position.PositionListRequest;
import com.goodt.drive.rtcore.dto.rostalent.position.PositionListResponse;
import com.goodt.drive.rtcore.dto.tasksetting2.FilterAwarePageResponse;
import com.goodt.drive.rtcore.dto.tasksetting2.filter.FilterDto;
import com.goodt.drive.rtcore.dto.tasksetting2.filter.FilterDtoFactory;
import com.goodt.drive.rtcore.security.AuthService;
import com.goodt.drive.rtcore.service.tasksetting2.task.SimpleTaskServiceImpl;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.application.util.UtilClass;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.TaskEntity;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.TaskFieldEntity;
import me.goodt.vkpht.module.orgstructure.api.IAssignmentService;
import me.goodt.vkpht.module.orgstructure.api.IPositionService;
import me.goodt.vkpht.module.orgstructure.api.IWorkFunctionService;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionGradeDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionGroupDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionGroupPositionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionImportanceReasonGroupDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionInputDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionKrLevelDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionPositionGradeDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionPositionImportanceInputDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionPositionKrLevelDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorRawDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorReadinessRawDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.AssignmentReadinessDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.LegalEntityTeamAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.OrgReasonDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionCategoryDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionGradeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionGroupDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionGroupPositionDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionImportanceDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionImportanceReasonGroupDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionKrLevelDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionPositionGradeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionPositionImportanceDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionPositionKrLevelDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionRankDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionSuccessorDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionSuccessorReadinessDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionTypeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.SystemRoleDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionCategoryFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionGradeFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionGroupFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionImportanceFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionRankFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionSuccessorFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionTypeFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.simple.DivisionSmp;
import me.goodt.vkpht.module.orgstructure.domain.dao.simple.DivisionTeamSmp;
import me.goodt.vkpht.module.orgstructure.domain.dao.simple.JobTitleSmp;
import me.goodt.vkpht.module.orgstructure.domain.dao.simple.PositionAssignmentSmp;
import me.goodt.vkpht.module.orgstructure.domain.dao.simple.PositionSmp;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentReadinessEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionCategoryEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGradeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGroupEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGroupPositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceReasonGroupEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionKrLevelEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionPositionGradeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionPositionImportanceEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionPositionKrLevelEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionRankEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorReadinessEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.composite.key.PositionPositionGradeId;
import me.goodt.vkpht.module.orgstructure.domain.entity.composite.key.PositionPositionKrLevelId;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PositionServiceImpl implements IPositionService {

    private final IWorkFunctionService workFunctionService;
    private final IAssignmentService assignmentService;
    private final PositionDao positionDao;
    private final PositionCategoryDao positionCategoryDao;
    private final PositionGroupDao positionGroupDao;
    private final PositionImportanceDao positionImportanceDao;
    private final PositionPositionImportanceDao positionPositionImportanceDao;
    private final PositionImportanceReasonGroupDao positionImportanceReasonGroupDao;
    private final PositionPositionGradeDao positionPositionGradeDao;
    private final PositionPositionKrLevelDao positionPositionKrLevelDao;
    private final PositionRankDao positionRankDao;
    private final PositionTypeDao positionTypeDao;
    private final OrgReasonDao orgReasonDao;
    private final SystemRoleDao systemRoleDao;
    private final NativeDao nativeDao;
    private final AssignmentReadinessDao assignmentReadinessDao;
    private final DivisionDao divisionDao;
    private final LegalEntityTeamAssignmentDao legalEntityTeamAssignmentDao;
    private final EmployeeDao employeeDao;
    private final PositionSuccessorDao positionSuccessorDao;
    private final PositionSuccessorReadinessDao positionSuccessorReadinessDao;
    private final PositionGradeDao positionGradeDao;
    private final PositionGroupPositionDao positionGroupPositionDao;
    private final PositionAssignmentDao positionAssignmentDao;
    private final PositionKrLevelDao positionKrLevelDao;
    private final DivisionTeamAssignmentDao divisionTeamAssignmentDao;
    private final DivisionTeamDao divisionTeamDao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;
    private final TaskFieldDao taskFieldDao;
    private final SimpleTaskServiceImpl taskService;
    @Override
    @Transactional(readOnly = true)
    public PositionTypeEntity findPositionType(Long id) throws NotFoundException {
        PositionTypeEntity entity = positionTypeDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("PositionTypeEntity with id %d not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionTypeEntity> findPositionTypeAll() {
        PositionTypeFilter filter = PositionTypeFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return positionTypeDao.find(filter);
    }

    @Override
    public PositionTypeEntity createPositionType(PositionTypeDto dto) {
        PositionTypeEntity entity = new PositionTypeEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUnitCode(unitAccessService.getCurrentUnit());

        Date currentDate = new Date();
        entity.setDateFrom(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        return positionTypeDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionTypeEntity updatePositionType(PositionTypeDto dto) throws NotFoundException {
        PositionTypeEntity entity = findPositionType(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        return positionTypeDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public void deletePositionType(Long id) throws NotFoundException {
        PositionTypeEntity entity = findPositionType(id);
        Date currentDate = new Date();
        entity.setUpdateDate(currentDate);
        entity.setDateTo(currentDate);
        positionTypeDao.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PositionKrLevelEntity findPositionKrLevel(Long id) throws NotFoundException {
        var entity = positionKrLevelDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("PositionKrLevelEntity with id %d not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionKrLevelEntity> findPositionKrLevelAll() {
        return positionKrLevelDao.findAllByUnitCode(unitAccessService.getCurrentUnit());
    }

    @Override
    public PositionKrLevelEntity createPositionKrLevel(PositionKrLevelDto dto) {
        PositionKrLevelEntity entity = new PositionKrLevelEntity(dto.getName(), dto.getDescription());
        return positionKrLevelDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionKrLevelEntity updatePositionKrLevel(PositionKrLevelDto dto) throws NotFoundException {
        PositionKrLevelEntity entity = findPositionKrLevel(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return positionKrLevelDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public Long deletePositionKrLevel(Long id) throws NotFoundException {
        PositionKrLevelEntity entity = findPositionKrLevel(id);
        positionKrLevelDao.delete(entity);
        return 1L;
    }

    @Override
    @Transactional(readOnly = true)
    public PositionPositionKrLevelEntity findPositionPositionKrLevel(Long positionId, Long positionKrLevelId) throws NotFoundException {
        PositionEntity position = positionDao
                .findById(positionId)
                .orElseThrow(() -> new NotFoundException(String.format("Position with id=%d is not found", positionId)));
        checkUnit(position);
        PositionKrLevelEntity positionKrLevelEntity = positionKrLevelDao.findById(positionKrLevelId)
                .orElseThrow(() -> new NotFoundException(String.format("PositionKrLevel with id=%d is not found", positionKrLevelId)));
        unitAccessService.checkUnitAccess(positionKrLevelEntity.getUnitCode());
        return positionPositionKrLevelDao
                .findById(new PositionPositionKrLevelId(position, positionKrLevelEntity))
                .orElseThrow(() -> new NotFoundException(String.format("PositionPositionKrLevel with position_id=%d and position_kr_level_id=%d is not found", positionId, positionKrLevelId)));
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionPositionKrLevelEntity createPositionPositionKrLevel(PositionPositionKrLevelDto dto) throws NotFoundException {
        PositionPositionKrLevelEntity entity = new PositionPositionKrLevelEntity();
        PositionEntity position = positionDao
                .findById(dto.getPositionId())
                .orElseThrow(() -> new NotFoundException(String.format("Position with id=%d is not found", dto.getPositionId())));
        checkUnit(position);
        PositionKrLevelEntity positionKrLevelEntity = positionKrLevelDao.findById(dto.getPositionKrLevelId())
                .orElseThrow(() -> new NotFoundException(String.format("PositionKrLevel with id=%d is not found", dto.getPositionKrLevelId())));
        unitAccessService.checkUnitAccess(positionKrLevelEntity.getUnitCode());
        entity.setId(new PositionPositionKrLevelId(position, positionKrLevelEntity));
        entity.setDateFrom(new Date());
        return positionPositionKrLevelDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public void deletePositionPositionKrLevel(Long positionId, Long positionKrLevelId) throws NotFoundException {
        PositionEntity position = positionDao
                .findById(positionId)
                .orElseThrow(() -> new NotFoundException(String.format("Position with id=%d is not found", positionId)));
        checkUnit(position);
        PositionKrLevelEntity positionKrLevelEntity = positionKrLevelDao.findById(positionKrLevelId)
                .orElseThrow(() -> new NotFoundException(String.format("PositionKrLevel with id=%d is not found", positionKrLevelId)));
        unitAccessService.checkUnitAccess(positionKrLevelEntity.getUnitCode());
        PositionPositionKrLevelEntity entity = positionPositionKrLevelDao
                .findById(new PositionPositionKrLevelId(position, positionKrLevelEntity))
                .orElseThrow(() -> new NotFoundException(String.format("PositionPositionKrLevel with position_id=%d and position_kr_level_id=%d is not found", positionId, positionKrLevelId)));
        entity.setDateTo(new Date());
        positionPositionKrLevelDao.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PositionGroupEntity findPositionGroup(Long id) throws NotFoundException {
        PositionGroupEntity entity = positionGroupDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("PositionGroupEntity with id %d not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionGroupEntity> findPositionGroupAll() {
        PositionGroupFilter filter = PositionGroupFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return positionGroupDao.find(filter);
    }

    @Override
    public PositionGroupEntity createPositionGroup(PositionGroupDto dto) {
        PositionGroupEntity entity = new PositionGroupEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDateFrom(new Date());
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        return positionGroupDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionGroupEntity updatePositionGroup(PositionGroupDto dto) throws NotFoundException {
        PositionGroupEntity entity = findPositionGroup(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return positionGroupDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public Long deletePositionGroup(Long id) throws NotFoundException {
        PositionGroupEntity entity = findPositionGroup(id);
        entity.setDateTo(new Date());
        positionGroupDao.save(entity);
        return 1L;
    }

    @Override
    @Transactional(readOnly = true)
    public PositionGroupPositionEntity findPositionGroupPosition(Long id) throws NotFoundException {
        return positionGroupPositionDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("PositionGroupPositionEntity with id %d not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionGroupPositionEntity> findPositionGroupPositionAll(Long positionId, Long positionGroupId) {
        Iterable<PositionGroupPositionEntity> positionGroupPositionEntityIterable;
        if (positionId != null && positionGroupId != null) {
            positionGroupPositionEntityIterable = positionGroupPositionDao.findAllByPositionIdAndPositionGroupId(positionId, positionGroupId);
        } else if (positionId != null) {
            positionGroupPositionEntityIterable = positionGroupPositionDao.findAllByPositionId(positionId);
        } else if (positionGroupId != null) {
            positionGroupPositionEntityIterable = positionGroupPositionDao.findAllByPositionGroupId(positionGroupId);
        } else {
            positionGroupPositionEntityIterable = positionGroupPositionDao.findAll();
        }
        return StreamSupport.stream(positionGroupPositionEntityIterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionGroupPositionEntity createPositionGroupPosition(PositionGroupPositionDto dto) throws NotFoundException {
        PositionGroupPositionEntity entity = new PositionGroupPositionEntity();
        PositionEntity positionEntity = positionDao
                .findById(dto.getPositionId())
                .orElseThrow(() -> new NotFoundException(String.format("Position with id=%d is not found", dto.getPositionId())));
        checkUnit(positionEntity);
        PositionGroupEntity positionGroupEntity = positionGroupDao
                .findById(dto.getPositionGroupId())
                .orElseThrow(() -> new NotFoundException(String.format("PositionGroup with id=%d is not found", dto.getPositionGroupId())));
        unitAccessService.checkUnitAccess(positionGroupEntity.getUnitCode());
        entity.setId(dto.getId());
        entity.setPosition(positionEntity);
        entity.setPositionGroup(positionGroupEntity);
        entity.setDateFrom(new Date());
        return positionGroupPositionDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public void deletePositionGroupPosition(Long id) throws NotFoundException {
        PositionGroupPositionEntity entity = findPositionGroupPosition(id);
        entity.setDateTo(new Date());
        positionGroupPositionDao.save(entity);
    }

    @Override
    public PositionGradeEntity findPositionGrade(Long id) throws NotFoundException {
        PositionGradeEntity entity = positionGradeDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("PositionGradeEntity with id %d not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionGradeEntity> findPositionGradeAll() {
        PositionGradeFilter filter = PositionGradeFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return positionGradeDao.find(filter);
    }

    @Override
    public PositionGradeEntity createPositionGrade(PositionGradeDto dto) {
        PositionGradeEntity entity = new PositionGradeEntity(dto.getName(), dto.getDescription(), dto.getIndex());
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        return positionGradeDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionGradeEntity updatePositionGrade(PositionGradeDto dto) throws NotFoundException {
        PositionGradeEntity entity = findPositionGrade(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIndex(dto.getIndex());
        return positionGradeDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public Long deletePositionGrade(Long id) throws NotFoundException {
        PositionGradeEntity entity = findPositionGrade(id);
        positionGradeDao.delete(entity);
        return 1L;
    }

    @Override
    @Transactional(readOnly = true)
    public PositionPositionGradeEntity findPositionPositionGrade(Long positionId, Long positionGradeId) throws NotFoundException {
        PositionEntity position = positionDao
                .findById(positionId)
                .orElseThrow(() -> new NotFoundException(String.format("Position with id=%d is not found", positionId)));
        checkUnit(position);
        PositionGradeEntity positionGradeEntity = positionGradeDao
                .findById(positionGradeId)
                .orElseThrow(() -> new NotFoundException(String.format("PositionGrade with id=%d is not found", positionGradeId)));
        return positionPositionGradeDao
                .findById(new PositionPositionGradeId(position, positionGradeEntity))
                .orElseThrow(() -> new NotFoundException(String.format("PositionPositionGrade with position_id=%d and position_kr_level_id=%d is not found", positionId, positionGradeId)));
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionPositionGradeEntity createPositionPositionGrade(PositionPositionGradeDto dto) throws NotFoundException {
        PositionPositionGradeEntity entity = new PositionPositionGradeEntity();
        PositionEntity position = positionDao
                .findById(dto.getPositionId())
                .orElseThrow(() -> new NotFoundException(String.format("Position with id=%d is not found", dto.getPositionId())));
        checkUnit(position);
        PositionGradeEntity positionGradeEntity = positionGradeDao
                .findById(dto.getPositionGradeId())
                .orElseThrow(() -> new NotFoundException(String.format("PositionGrade with id=%d is not found", dto.getPositionGradeId())));
        entity.setId(new PositionPositionGradeId(position, positionGradeEntity));
        entity.setDateFrom(new Date());
        return positionPositionGradeDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public void deletePositionPositionGrade(Long positionId, Long positionGradeId) throws NotFoundException {
        PositionEntity position = positionDao
                .findById(positionId)
                .orElseThrow(() -> new NotFoundException(String.format("Position with id=%d is not found", positionId)));
        checkUnit(position);
        PositionGradeEntity positionGradeEntity = positionGradeDao
                .findById(positionGradeId)
                .orElseThrow(() -> new NotFoundException(String.format("PositionGrade with id=%d is not found", positionGradeId)));
        PositionPositionGradeEntity entity = positionPositionGradeDao
                .findById(new PositionPositionGradeId(position, positionGradeEntity))
                .orElseThrow(() -> new NotFoundException(String.format("PositionPositionGrade with position_id=%d and position_kr_level_id=%d is not found", positionId, positionGradeId)));
        entity.setDateTo(new Date());
        positionPositionGradeDao.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PositionImportanceReasonGroupEntity findPositionImportanceReasonGroup(Long id) throws NotFoundException {
        return positionImportanceReasonGroupDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("PositionImportanceReasonGroupEntity with id %d not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionImportanceReasonGroupEntity> findPositionImportanceReasonGroupAll() {
        return StreamSupport.stream(positionImportanceReasonGroupDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public PositionImportanceReasonGroupEntity createPositionImportanceReasonGroup(PositionImportanceReasonGroupDto dto) {
        PositionImportanceReasonGroupEntity entity = new PositionImportanceReasonGroupEntity(dto.getName(), dto.getDescription(), dto.getIsChangeable());
        return positionImportanceReasonGroupDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionImportanceReasonGroupEntity updatePositionImportanceReasonGroup(PositionImportanceReasonGroupDto dto) throws NotFoundException {
        PositionImportanceReasonGroupEntity entity = findPositionImportanceReasonGroup(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIsChangeable(dto.getIsChangeable());
        return positionImportanceReasonGroupDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public Long deletePositionImportanceReasonGroup(Long id) throws NotFoundException {
        PositionImportanceReasonGroupEntity entity = findPositionImportanceReasonGroup(id);
        positionImportanceReasonGroupDao.delete(entity);
        return 1L;
    }

    @Override
    @Transactional(readOnly = true)
    public PositionSuccessorEntity findPositionSuccessor(Long id) throws NotFoundException {
        PositionSuccessorEntity entity = positionSuccessorDao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("PositionSuccessorEntity with id %d not found", id)));
        if (entity.getReasonInclusion() != null) {
            unitAccessService.checkUnitAccess(entity.getReasonInclusion().getUnitCode());
        }
        if (entity.getReasonExclusion() != null) {
            unitAccessService.checkUnitAccess(entity.getReasonExclusion().getUnitCode());
        }
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionSuccessorEntity> findPositionSuccessorAll() {
        return positionSuccessorDao.findAll(PositionSuccessorFilter.builder()
            .actual(false)
            .build()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PositionSuccessorEntity getPositionSuccessorById(Long id) throws NotFoundException {
        PositionSuccessorEntity entity = positionSuccessorDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("position_successor with id=%d is not found", id)));
        if (entity.getReasonInclusion() != null) {
            unitAccessService.checkUnitAccess(entity.getReasonInclusion().getUnitCode());
        }
        if (entity.getReasonExclusion() != null) {
            unitAccessService.checkUnitAccess(entity.getReasonExclusion().getUnitCode());
        }
        return entity;
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionSuccessorEntity createPositionSuccessor(PositionSuccessorRawDto dto) throws NotFoundException {
        EmployeeEntity employee = null;
        if (dto.getEmployeeId() != null) {
            employee = employeeDao.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new NotFoundException(String.format("EmployeeEntity with id %d not found", dto.getEmployeeId())));
        }
        PositionEntity position = null;
        if (dto.getPositionId() != null) {
            position = positionDao.findById(dto.getPositionId())
                    .orElseThrow(() -> new NotFoundException(String.format("PositionEntity with id %d not found", dto.getPositionId())));
            checkUnit(position);
        }
        PositionGroupEntity positionGroup = null;
        if (dto.getPositionGroupId() != null) {
            positionGroup = positionGroupDao.findById(dto.getPositionGroupId())
                    .orElseThrow(() -> new NotFoundException(String.format("PositionGroupEntity with id %d not found", dto.getPositionGroupId())));
            unitAccessService.checkUnitAccess(positionGroup.getUnitCode());
        }
        OrgReasonEntity reasonInclusion = null;
        if (dto.getReasonInclusionId() != null) {
            reasonInclusion = orgReasonDao.findById(dto.getReasonInclusionId())
                    .orElseThrow(() -> new NotFoundException(String.format("ReasonEntity with id %d (reason inclusion) not found", dto.getReasonInclusionId())));
            unitAccessService.checkUnitAccess(reasonInclusion.getUnitCode());
        }
        OrgReasonEntity reasonExclusion = null;
        if (dto.getReasonExclusionId() != null) {
            reasonExclusion = orgReasonDao.findById(dto.getReasonExclusionId())
                    .orElseThrow(() -> new NotFoundException(String.format("ReasonEntity with id %d (reason exclusion) not found", dto.getReasonExclusionId())));
            unitAccessService.checkUnitAccess(reasonExclusion.getUnitCode());
        }
        PositionSuccessorEntity entity = new PositionSuccessorEntity(dto.getDateCommitHr(), dto.getDatePriority(), employee,
                                                                     position, positionGroup,
                                                                     reasonInclusion, reasonExclusion,
                                                                     dto.getCommentInclusion(), dto.getCommentExclusion(),
                                                                     dto.getDocumentUrlInclusion(), dto.getDocumentUrlExclusion(),
                                                                     new Date(), null);

        return positionSuccessorDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionSuccessorEntity updatePositionSuccessor(Long id, PositionSuccessorRawDto dto) throws NotFoundException {
        PositionSuccessorEntity entity = findPositionSuccessor(id);
        if (dto.getEmployeeId() != null) {
            EmployeeEntity employeeEntity = employeeDao.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new NotFoundException(String.format("EmployeeEntity with id %d not found", dto.getEmployeeId())));
            entity.setEmployee(employeeEntity);
        }
        if (dto.getPositionId() != null) {
            PositionEntity positionEntity = positionDao
                    .findById(dto.getPositionId())
                    .orElseThrow(() -> new NotFoundException(String.format("PositionEntity with id %d not found", dto.getPositionId())));
            checkUnit(positionEntity);
            entity.setPosition(positionEntity);
        }
        if (dto.getPositionGroupId() != null) {
            PositionGroupEntity positionGroupEntity = positionGroupDao
                    .findById(dto.getPositionGroupId())
                    .orElseThrow(() -> new NotFoundException(String.format("PositionGroupEntity with id %d not found", dto.getPositionGroupId())));
            unitAccessService.checkUnitAccess(positionGroupEntity.getUnitCode());
            entity.setPositionGroup(positionGroupEntity);
        }
        if (dto.getReasonInclusionId() != null) {
            OrgReasonEntity orgReasonEntity = orgReasonDao
                    .findById(dto.getReasonInclusionId())
                    .orElseThrow(() -> new NotFoundException(String.format("ReasonEntity with id %d (reason inclusion) not found", dto.getReasonInclusionId())));
            unitAccessService.checkUnitAccess(orgReasonEntity.getUnitCode());
            entity.setReasonInclusion(orgReasonEntity);
        }
        if (dto.getReasonExclusionId() != null) {
            OrgReasonEntity orgReasonEntity = orgReasonDao
                    .findById(dto.getReasonExclusionId())
                    .orElseThrow(() -> new NotFoundException(String.format("ReasonEntity with id %d (reason exclusion) not found", dto.getReasonExclusionId())));
            unitAccessService.checkUnitAccess(orgReasonEntity.getUnitCode());
            entity.setReasonExclusion(orgReasonEntity);
        }
        entity.setDateCommitHr(dto.getDateCommitHr());
        entity.setDatePriority(dto.getDatePriority());
        entity.setCommentInclusion(dto.getCommentInclusion());
        entity.setCommentExclusion(dto.getCommentExclusion());
        entity.setDocumentUrlInclusion(dto.getDocumentUrlInclusion());
        entity.setDocumentUrlExclusion(dto.getDocumentUrlExclusion());
        positionSuccessorDao.save(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public Long deletePositionSuccessor(Long id) throws NotFoundException {
        PositionSuccessorEntity entity = findPositionSuccessor(id);
        entity.setDateTo(new Date());
        positionSuccessorDao.save(entity);
        return 1L;
    }

    @Override
    @Transactional(readOnly = true)
    public PositionSuccessorReadinessEntity findPositionSuccessorReadiness(Long id) throws NotFoundException {
        PositionSuccessorReadinessEntity entity = positionSuccessorReadinessDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("PositionSuccessorReadinessEntity with id %d not found", id)));
        if (entity.getReadiness() != null) {
            unitAccessService.checkUnitAccess(entity.getReadiness().getUnitCode());
        }
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionSuccessorReadinessEntity> findPositionSuccessorReadinessAll() {
        return positionSuccessorReadinessDao.findAll(unitAccessService.getCurrentUnit());
    }

    @Override
    @Transactional(readOnly = true)
    public PositionSuccessorReadinessEntity getPositionSuccessorReadinessById(Long id) throws NotFoundException {
        PositionSuccessorReadinessEntity entity = positionSuccessorReadinessDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("position_successor_readiness with id=%d is not found", id)));
        if (entity.getReadiness() != null) {
            unitAccessService.checkUnitAccess(entity.getReadiness().getUnitCode());
        }
        return entity;
    }

    @Override
    public PositionSuccessorReadinessEntity createPositionSuccessorReadiness(PositionSuccessorReadinessRawDto dto) {
        PositionSuccessorEntity positionSuccessor = null;
        if (dto.getPositionSuccessorId() != null) {
            Optional<PositionSuccessorEntity> positionSuccessorEntityOptional = positionSuccessorDao.findById(dto.getPositionSuccessorId());
            if (positionSuccessorEntityOptional.isPresent()) {
                positionSuccessor = positionSuccessorEntityOptional.get();
                if (positionSuccessor.getReasonExclusion() != null) {
                    unitAccessService.checkUnitAccess(positionSuccessor.getReasonExclusion().getUnitCode());
                }
                if (positionSuccessor.getReasonInclusion() != null) {
                    unitAccessService.checkUnitAccess(positionSuccessor.getReasonInclusion().getUnitCode());
                }
            }
        }
        AssignmentReadinessEntity readiness = null;
        if (dto.getReadinessId() != null) {
            Optional<AssignmentReadinessEntity> assignmentReadinessEntityOptional = assignmentReadinessDao.findById(dto.getReadinessId());
            if (assignmentReadinessEntityOptional.isPresent()) {
                readiness = assignmentReadinessEntityOptional.get();
                unitAccessService.checkUnitAccess(readiness.getUnitCode());
            }
        }
        return positionSuccessorReadinessDao.save(new PositionSuccessorReadinessEntity(positionSuccessor, readiness, new Date(), null));
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionSuccessorReadinessEntity updatePositionSuccessorReadiness(PositionSuccessorReadinessRawDto dto) throws NotFoundException {
        PositionSuccessorReadinessEntity entity = findPositionSuccessorReadiness(dto.getId());
        if (dto.getPositionSuccessorId() != null) {
            positionSuccessorDao.findById(dto.getPositionSuccessorId()).ifPresent(el -> {
                if (el.getReasonInclusion() != null)
                    unitAccessService.checkUnitAccess(el.getReasonInclusion().getUnitCode());
                if (el.getReasonExclusion() != null)
                    unitAccessService.checkUnitAccess(el.getReasonExclusion().getUnitCode());
                entity.setPositionSuccessor(el);
            });
        }
        if (dto.getReadinessId() != null) {
            assignmentReadinessDao.findById(dto.getReadinessId()).ifPresent(el -> {
                unitAccessService.checkUnitAccess(el.getUnitCode());
                entity.setReadiness(el);
            });
        }
        return positionSuccessorReadinessDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public Long deletePositionSuccessorReadiness(Long id) throws NotFoundException {
        PositionSuccessorReadinessEntity entity = findPositionSuccessorReadiness(id);
        entity.setDateTo(new Date());
        positionSuccessorReadinessDao.save(entity);
        return 1L;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionCategoryEntity> findPositionCategoryAll() {
        PositionCategoryFilter filter = PositionCategoryFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return positionCategoryDao.find(filter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionRankEntity> findPositionRankAll() {
        PositionRankFilter filter = PositionRankFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return positionRankDao.find(filter);
    }

    @Override
    @Transactional(readOnly = true)
    public PositionEntity getPosition(Long positionId) throws NotFoundException {
        PositionEntity entity = positionDao.findById(positionId)
            .orElseThrow(() -> new NotFoundException(String.format("Position with id: %d is not found", positionId)));
        checkUnit(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionEntity updatePosition(Long positionId, PositionInputDto dto) throws NotFoundException {
        PositionEntity position = getPosition(positionId);
        position.setWorkFunction(dto.getWorkFunctionId() != null ? workFunctionService.get(dto.getWorkFunctionId()) : null);
        position.setPositionType(dto.getPositionTypeId() != null ? findPositionType(dto.getPositionTypeId()) : null);
        position.setUpdateDate(new Date());
        position.setUpdateEmployeeId(authService.getUserEmployeeId());
        return positionDao.save(position);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionEntity> getPositionByEmployeeIdAndDivisionIds(Long employeeId, List<Long> divisionIds) {
        return positionDao.getPositionByEmployeeIdAndDivisionIds(employeeId, divisionIds, unitAccessService.getCurrentUnit());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionAssignmentSmp> getAllByDivision(Long divisionTeamId, boolean withChildren) {
        List<DivisionTeamTree> tree = nativeDao.findAllChildrenDivTeam(divisionTeamId);
        List<Long> divisionTeams = tree.stream().map(DivisionTeamTree::getId).collect(toList());
        final QPositionAssignmentEntity qpa = QPositionAssignmentEntity.positionAssignmentEntity;
        final QDivisionEntity qd = QDivisionEntity.divisionEntity;
        final QDivisionTeamShort qdt = QDivisionTeamShort.divisionTeamShort;
        final QJobTitleEntity qjt = QJobTitleEntity.jobTitleEntity;
        final QPositionEntity qp = QPositionEntity.positionEntity;
        JPQLQuery<?> resultQuery;
        if (withChildren) {
            // найти всех сотрудников в командах divisionTeams и по сотрудникам и командам найти PositionAssignment
            resultQuery = positionDao.findAllByDivisionOrManager(divisionTeams, unitAccessService.getCurrentUnit());
        } else {
            List<Long> employeeIds = new ArrayList<>();
            // найти сотрудников по переданной команде и её дочерним командам
            getEmployeeIdsRecursion(employeeIds, divisionTeamId);
            // по сотрудникам и командам найти PositionAssignment
            resultQuery = positionDao.findAllByDivisionTeamAndEmployee(employeeIds, divisionTeams, unitAccessService.getCurrentUnit());
        }
        List<Tuple> tuples = resultQuery.select(qpa.id, qpa.dateFrom, qpa.dateTo, qpa.employeeId, qpa.typeId, qpa.shortName, qpa.fullName,
                                                qd.id, qd.parentId, qd.legalEntityId, qd.fullName, qd.shortName,
                                                qjt.id, qjt.fullName, qjt.shortName,
                                                qp.id, qp.dateFrom, qp.dateTo, qp.fullName, qp.shortName, qp.categoryId, qp.positionImportanceId,
                                                qdt.id, qdt.fullName, qdt.shortName)
                .fetch();
        List<PositionAssignmentSmp> rez = new ArrayList<>(tuples.size());
        for (Tuple t : tuples) {
            PositionSmp pos = new PositionSmp().setId(t.get(qp.id)).setDateFrom(t.get(qp.dateFrom)).setDateTo(t.get(qp.dateTo))
                    .setFullName(t.get(qp.fullName)).setShortName(t.get(qp.shortName)).setCategoryId(t.get(qp.categoryId))
                    .setPositionImportanceId(t.get(qp.positionImportanceId));
            JobTitleSmp jt = new JobTitleSmp().setId(t.get(qjt.id)).setFullName(t.get(qjt.fullName)).setShortName(t.get(qjt.shortName));
            DivisionSmp dv = new DivisionSmp().setId(t.get(qd.id)).setParentId(t.get(qd.parentId))
                    .setShortName(t.get(qd.shortName)).setFullName(t.get(qd.fullName)).setLegalEntityId(t.get(qd.legalEntityId));
            PositionAssignmentSmp pa = new PositionAssignmentSmp()
                    .setId(t.get(qpa.id)).setDateFrom(t.get(qpa.dateFrom)).setDateTo(t.get(qpa.dateTo))
                    .setEmployeeId(t.get(qpa.employeeId)).setTypeId(t.get(qpa.typeId))
                    .setShortName(t.get(qpa.shortName)).setFullName(t.get(qpa.fullName));
            DivisionTeamSmp dt = new DivisionTeamSmp().setId(t.get(qdt.id)).setFullName(t.get(qdt.fullName)).setShortName(t.get(qdt.shortName));
            pa.setPosition(pos).setDivision(dv).setJobTitle(jt).setDivisionTeam(dt);
            // pa.addModel("position", pos).addModel("division", dv).addModel("jobTitle",
            // jt);
            rez.add(pa);
        }
        return rez;
    }

    @Override
    @Transactional(readOnly = true)
    public PositionAssignmentEntity getPositionAssignmentByPositionId(Long positionId) {
        PositionAssignmentEntity entity = positionAssignmentDao.getPositionAssignmentByPositionId(positionId);
        if (entity.getSubstitutionType() != null) {
            unitAccessService.checkUnitAccess(entity.getSubstitutionType().getUnitCode());
        }
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, List<PositionAssignmentEntity>> findActualPositionAssignmentByEmployeeIds(Collection<Long> employeeIds) {
        if (CollectionUtils.isEmpty(employeeIds)) {
            return Map.of();
        }
        return positionAssignmentDao.findActualByEmployeeIds(List.copyOf(employeeIds), unitAccessService.getCurrentUnit());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionImportanceEntity> findPositionImportanceAll() {
        return positionImportanceDao.findAll(PositionImportanceFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build());
    }

    @Override
    @Transactional(readOnly = true)
    public PositionImportanceEntity getPositionImportance(Integer id) throws NotFoundException {
        PositionImportanceEntity entity = positionImportanceDao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("PositionImportanceEntity with id %d not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public PositionPositionImportanceEntity getPositionPositionImportance(Long id) throws NotFoundException {
        PositionPositionImportanceEntity entity = positionPositionImportanceDao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("PositionPositionImportanceEntity with id %d not found", id)));
        if (entity.getPositionImportance() != null) {
            unitAccessService.checkUnitAccess(entity.getPositionImportance().getUnitCode());
        }
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionPositionImportanceEntity> getAllPositionPositionImportance(boolean withClosed, List<Long> divisionTeamIds, List<Long> userIds, List<Long> positionIds) {
        return positionPositionImportanceDao.findAllPositionPositionImportanceByDivisionTeamAndUsers(divisionTeamIds, userIds, positionIds, withClosed, unitAccessService.getCurrentUnit());
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionPositionImportanceEntity createPositionPositionImportance(PositionPositionImportanceInputDto dto, String externalId) throws NotFoundException {
        List<PositionPositionImportanceEntity> actualByPositionId = positionPositionImportanceDao.findActualByPositionId(dto.getPosition(), unitAccessService.getCurrentUnit());
        if (!actualByPositionId.isEmpty()) {
            actualByPositionId.forEach(item -> item.setDateTo(new Date()));
            positionPositionImportanceDao.saveAll(actualByPositionId);
        }

        List<PositionPositionImportanceEntity> list = positionPositionImportanceDao.findActualByPositionId(dto.getPosition(), unitAccessService.getCurrentUnit());
        for (PositionPositionImportanceEntity item : list) {
            item.setDateTo(new Date());
            positionPositionImportanceDao.save(item);
        }

        PositionPositionImportanceEntity entity = new PositionPositionImportanceEntity();
        entity.setDateFrom(new Date());
        entity.setPosition(this.getPosition(dto.getPosition()));
        entity.setSystemRoleId(systemRoleDao.findById(dto.getSystemRoleId())
                                       .orElseThrow(() -> new NotFoundException(String.format("System role with id=%d is not found", dto.getSystemRoleId()))));

        EmployeeEntity employee = employeeDao.employeeByExternalId(externalId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Employee with external id=%s is not found", externalId)));
        entity.setAuthorEmployee(employee);

        if (dto.getSystemRoleId() == 1) {
            List<DivisionTeamAssignmentDto> divisionTeamAssignmentDtos = assignmentService.getDivisionTeamAssignments(null, Collections.singletonList(employee.getId()), null, null);
            List<Long> divisionTeamsIds = new ArrayList<>();

            for (DivisionTeamAssignmentDto element : divisionTeamAssignmentDtos) {
                if (element.getDivisionTeamRole().getRole().getSystemRole().getId().equals(1)) {
                    divisionTeamsIds.add(element.getDivisionTeamRole().getDivisionTeam().getId());
                }
            }

            if (divisionTeamsIds.isEmpty()) {
                throw new NotFoundException(String.format("employee_id=%d has no system_role_id=1", employee.getId()));
            }

            Set<Long> teamDescendantIds = findTeamDescendantIds(divisionTeamsIds);
            divisionTeamsIds.addAll(teamDescendantIds);

            List<Long> subordinateEmployeeIdList = new ArrayList<>();

            for (Long id : divisionTeamsIds) {
                subordinateEmployeeIdList.addAll(
                    assignmentService.getDivisionTeamSubordinates(employee.getId(), null, id, true)
                                .stream()
                                .map(sub -> sub.getEmployee().getId())
                                .collect(toList())
                );
            }

            Set<Long> positionIds = new HashSet<>(positionDao.findActualIdsByEmployeeIds(subordinateEmployeeIdList, unitAccessService.getCurrentUnit()));

            if (!positionIds.contains(dto.getPosition())) {
                throw new NotFoundException(String.format("position_id=%d is not founded in Head's division_teams", dto.getPosition()));
            }

            if (dto.getPositionImportance() != null) {
                PositionImportanceEntity positionImportance = this.getPositionImportance(dto.getPositionImportance());
                unitAccessService.checkUnitAccess(positionImportance.getUnitCode());
                entity.setPositionImportance(positionImportance);
            }

        } else if (dto.getSystemRoleId().equals(3) || dto.getSystemRoleId().equals(4)) {
            Long legalEntityId = divisionDao.findLegalEntityIdByPositionId(dto.getPosition(), unitAccessService.getCurrentUnit());
            List<LegalEntityTeamAssignmentEntity> legalEntityTeamAssignmentEntityList = legalEntityTeamAssignmentDao.findByEmployeeId(employee.getId());
            Set<Long> legalEntityIdsList = new HashSet<>();

            for (LegalEntityTeamAssignmentEntity element : legalEntityTeamAssignmentEntityList) {
                if (element.getRole().getSystemRole().getId().equals(3) || element.getRole().getSystemRole().getId().equals(4)) {
                    legalEntityIdsList.add(element.getLegalEntityTeamEntity().getLegalEntityEntity().getId());
                }
            }

            if (legalEntityIdsList.contains(legalEntityId)) {
                entity.setPositionImportance(dto.getPositionImportance() != null ? this.getPositionImportance(dto.getPositionImportance()) : null);
            } else {
                throw new NotFoundException(String.format("position_id=%d is not founded in legal_entity_id=%d", dto.getPosition(), legalEntityId));
            }
        }

        return positionPositionImportanceDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionPositionImportanceEntity updatePositionPositionImportance(Long id, PositionPositionImportanceInputDto dto, String externalId) throws NotFoundException {
        deletePositionPositionImportance(id);
        return createPositionPositionImportance(dto, externalId);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public Long deletePositionPositionImportance(Long id) throws NotFoundException {
        PositionPositionImportanceEntity entity = getPositionPositionImportance(id);
        entity.setDateTo(new Date());
        positionPositionImportanceDao.save(entity);
        return 1L;
    }

    private void checkUnit(PositionEntity entity) {
        if (!entity.getDivision().getLegalEntityEntity().getUnitCode().equals(unitAccessService.getCurrentUnit())) {
            throw new NotFoundException(String.format("Сущность Position c id = %d не была найдена", entity.getId()));
        }
    }

    /**
     * Найти всех сотрудников в команде кроме руководителя, а также найти в дочерних командах руководителей и если их там нет,
     * то взять всех сотрудников и опускаться ниже пока не найдутся руководители
     *
     * @param employeeIds    массив ИД сотрудников для заполнения
     * @param divisionTeamId ИД команды
     */
    public void getEmployeeIdsRecursion(Collection<Long> employeeIds, Long divisionTeamId) {
        // найти всем сотрудников в команде, кроме рук-ля
        employeeIds.addAll(divisionTeamAssignmentDao.findEmployeeIdByDivisionTeamIdAndNotSystemRoleId(divisionTeamId, 1));
        List<Long> childrenTeam = divisionTeamDao.findActualIdsByParentId(divisionTeamId);
        Map<Long, Long> headEmployeeIdByChildrenTeam = divisionTeamAssignmentDao.findEmployeeIdByDivisionTeamIdsAndSystemRoleId(childrenTeam, 1)
                .stream()
                .collect(groupingBy(t -> t.get(0, Long.class), mapping(t -> t.get(1, Long.class), collectingAndThen(toList(), list -> list.get(0)))));

        childrenTeam.forEach(teamId -> {
            Long head = headEmployeeIdByChildrenTeam.get(teamId);
            if (head != null) {
                employeeIds.add(head);
            } else {
                getEmployeeIdsRecursion(employeeIds, teamId);
            }
        });
    }

    private Set<Long> findTeamDescendantIds(List<Long> divisionTeamsIds) {
        Set<Long> childIds = new HashSet<>();

        List<Long> parentIds = divisionTeamsIds;
        while (!parentIds.isEmpty()) {
            parentIds = divisionTeamDao.findAllChildrenIds(parentIds);
            childIds.addAll(parentIds);
        }

        return childIds;
    }

    @Override
    @Transactional(readOnly = true)
    public FilterAwarePageResponse<PositionListResponse> bffPositionList(PositionListRequest req) {
        String unit = unitAccessService.getCurrentUnit();
        if (!req.getUseFilter()) {
            req.setPositionName(null);
        }

        Page<PositionListResponse> response = positionDao.positionList(
            req.getDivisionId(),
            req.getPositionName(),
            PageRequest.of(req.getPage(), req.getSize()));

        Set<Long> competenceProfileTask = response.stream()
            .map(PositionListResponse::getCompetenceProfileId)
            .filter(Objects::nonNull)
            .collect(toSet());

        Set<Long> positionRelationTaskIds = response.stream()
            .map(PositionListResponse::getPositionRelationTaskId)
            .filter(Objects::nonNull)
            .collect(toSet());

        Map<Long, TaskFieldEntity> commits = taskFieldDao.findAllByTaskIdIn(
                positionRelationTaskIds,
                ComponentFieldCode.POSITION_RELATION_COMMIT,
                unitAccessService.getCurrentUnit())
            .stream()
            .collect(Collectors.toMap(x -> x.getTask().getId(), Function.identity(), (ex, rep) -> rep));

        for (PositionListResponse positionInfo : response) {
            if (positionInfo.getEmployeeId() != null) {
                String fullName = UtilClass.getFIOFromEmployee(
                    positionInfo.getSurname(),
                    positionInfo.getName(),
                    positionInfo.getPatronymic());
                positionInfo.setEmployeeFullName(fullName);
            }

            if (positionInfo.getPositionRelationTaskId() == null) {
                continue;
            }

            TaskFieldEntity commitField = commits.get(positionInfo.getPositionRelationTaskId());
            TaskEntity positionRelationTask = taskService.getById(positionInfo.getPositionRelationTaskId());

            positionInfo.setCompetenceProfileId(positionRelationTask.getParentId());

            if (commitField == null || !Boolean.parseBoolean(commitField.getValue())) {
                positionInfo.setCompetenceProfileId(null);
                positionInfo.setCompetenceProfileName(null);
                continue;
            }

            TaskFieldEntity profileName = taskFieldDao.findByTaskIdAndComponentCode(
                positionInfo.getCompetenceProfileId(),
                ComponentFieldCode.POSITION_PROFILE_NAME,
                unit
            );

            if (profileName != null) {
                positionInfo.setCompetenceProfileName(profileName.getValue());
            }
        }

        FilterAwarePageResponse<PositionListResponse> filterAwareResponse = new FilterAwarePageResponse<>(response);

        if (req.getUseFilter()) {
            FilterDto filter = FilterDtoFactory.createTextFilter("positionName", "Найти по названию позиции");
            filterAwareResponse.setFilters(List.of(filter));
        }
        return filterAwareResponse;
    }
}
