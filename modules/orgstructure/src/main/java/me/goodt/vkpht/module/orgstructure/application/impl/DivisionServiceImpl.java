package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.extern.slf4j.Slf4j;

import me.goodt.vkpht.common.api.exception.BadRequestException;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentRotationShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeSearchResult;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamAssignmentFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamRoleFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import com.goodt.drive.rtcore.dto.rostalent.DataForKafkaMessageInputDto;
import com.goodt.drive.rtcore.dto.rostalent.SuccessorNotificateByCodeInputData;
import com.goodt.drive.rtcore.service.notification.NotificationService;
import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionShortInfo;
import me.goodt.vkpht.common.api.dto.PositionInfo;
import me.goodt.vkpht.common.api.exception.ForbiddenException;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.application.util.EntityDateSelector;
import me.goodt.vkpht.common.application.util.GlobalDefs;
import me.goodt.vkpht.common.domain.dao.NativeDao;
import me.goodt.vkpht.common.domain.dao.filter.PositionAssignmentFilter;
import me.goodt.vkpht.common.domain.entity.DomainObject;
import me.goodt.vkpht.module.orgstructure.api.AssignmentService;
import me.goodt.vkpht.module.orgstructure.api.DivisionService;
import me.goodt.vkpht.module.orgstructure.api.DivisionTeamAssignmentRotationService;
import me.goodt.vkpht.module.orgstructure.api.EmployeeService;
import me.goodt.vkpht.module.orgstructure.api.dto.AssignmentRotationStatDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionFindDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfo;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionPathData;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentFindRequestDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentRotationDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleAndImportanceStatDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleContainerDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamStatDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorReadinessDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorsStatDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamWithRolesAndSuccessorsStatDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionWithDivisionTeamsStatDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionWithDivisionTeamsWithDivisionTeamRolesAndDivisionTeamSuccessorsDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeTeamInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.ImportanceStatDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionExtendedDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorReadinessDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.AssignmentReadinessDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.AssignmentRotationDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionGroupDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionLinksDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamAssignmentRotationDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamRoleDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamSuccessorDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamSuccessorReadinessDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.LegalEntityDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionGradeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionKrLevelDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionSuccessorReadinessDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.RoleDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.DivisionFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentReadinessEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentRotationEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionGroupEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentRotationEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentShort;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamRoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamSuccessorEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamSuccessorReadinessEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGradeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionKrLevelEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorReadinessEntity;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionInfoFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamAssignmentRotationFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamSuccessorFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamSuccessorReadinessFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionSuccessorFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionSuccessorReadinessFactory;

import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@Transactional
public class DivisionServiceImpl implements DivisionService {

    public static final String MESSAGE_CREATE_DIVISION_TEAM_ASSIGNMENT_ROTATION = "Пожалуйста, актуализируйте свой статус ротации.";
    public static final String MESSAGE_CREATE_DIVISION_TEAM_SUCCESSOR = "На позицию %s был назначен новый преемник.";
    public static final String SUBJECT_CREATE_DIVISION_TEAM_ASSIGNMENT_ROTATION = "Уведомление о создании записи с ротацией";
    public static final String SUBJECT_CREATE_DIVISION_TEAM_SUCCESSOR = "Уведомление о создании записи с преемником";

    private static final int READY = 1;
    private static final int READY_IN_ONE_YEAR = 2;
    private static final int READY_IN_TWO_YEARS = 3;

    @Autowired
    private DivisionTeamAssignmentRotationService divisionTeamAssignmentRotationService;
    @Autowired
    private DivisionTeamAssignmentDao divisionTeamAssignmentDao;
    @Autowired
    private DivisionTeamAssignmentRotationDao divisionTeamAssignmentRotationDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private PositionDao positionDao;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AssignmentReadinessDao assignmentReadinessDao;
    @Autowired
    private AssignmentRotationDao assignmentRotationDao;
    @Autowired
    private DivisionTeamSuccessorDao divisionTeamSuccessorDao;
    @Autowired
    private DivisionTeamSuccessorReadinessDao divisionTeamSuccessorReadinessDao;
    @Autowired
    private DivisionDao divisionDao;
    @Autowired
    private DivisionGroupDao divisionGroupDao;
    @Autowired
    private DivisionTeamDao divisionTeamDao;
    @Autowired
    private DivisionTeamRoleDao divisionTeamRoleDao;
    @Autowired
    private PositionAssignmentDao positionAssignmentDao;
    @Autowired
    private NativeDao nativeDao;
    @Autowired
    private PositionSuccessorReadinessDao positionSuccessorReadinessDao;
    @Autowired
    private PositionGradeDao positionGradeDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private LegalEntityDao legalEntityDao;
    @Autowired
    private DivisionLinksDao divisionLinksDao;
    @Autowired
    private PositionKrLevelDao positionKrLevelDao;
    @Autowired
    private AuthService authService;
    @Autowired
    private UnitAccessService unitAccessService;

    @Override
    @Transactional(readOnly = true)
    public DivisionEntity getDivision(Long id) throws NotFoundException {
        if (id == null) {
            throw new NotFoundException("division id is null");
        }

        DivisionEntity entity = divisionDao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("division %d is not found", id)));
        unitAccessService.checkUnitAccess(entity.getLegalEntityEntity().getUnitCode());
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionInfo> getDivisionPositions(Long divisionId) {
        List<PositionInfo> divisionPositionsInfo = new ArrayList<>();
        // todo: UMV: filter deleted postions
        List<PositionEntity> positions = EntityDateSelector.selectActual(positionDao.getPositionByDivisionId(divisionId, unitAccessService.getCurrentUnit()));
        List<Long> positionIds = positions.stream().map(DomainObject::getId).collect(Collectors.toList());
        List<PositionAssignmentEntity> assignments = EntityDateSelector.selectActual(
            positionAssignmentDao.findAll(
                PositionAssignmentFilter.builder()
                    .unitCode(unitAccessService.getCurrentUnit())
                    .positions(positionIds)
                    .build())
        );
        positions.forEach(p -> {
            List<PositionAssignmentEntity> current = assignments.stream().filter(a -> Objects.equals(a.getPositionId(), p.getId()))
                    .collect(Collectors.toList());
            divisionPositionsInfo.add(new PositionInfo(p, current));
        });
        return divisionPositionsInfo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionExtendedDto> getExtendedPositions(List<Long> divisionIds, List<Long> positionIds, boolean hasPositionAssignment, Long legalEntityId) {
        List<PositionExtendedDto> result = new ArrayList<>();
        Set<PositionEntity> positions = positionDao.getActualByIdsOrDivisionIdsOrLegalEntityId(positionIds, divisionIds,
            hasPositionAssignment, legalEntityId, unitAccessService.getCurrentUnit());

        positions.forEach(p -> {
            List<PositionSuccessorReadinessEntity> readiness = positionSuccessorReadinessDao.findByPositionSuccessorPositionId(p.getId(), unitAccessService.getCurrentUnit());
            List<DivisionTeamEntity> divisionTeams = divisionTeamDao.findByDivisionId(p.getDivisionId());
            List<PositionAssignmentEntity> assignments = positionAssignmentDao.findAll(
                PositionAssignmentFilter.builder()
                    .positionId(p.getId())
                    .unitCode(unitAccessService.getCurrentUnit())
                    .build()
            );
            List<PositionGradeEntity> positionGrades = positionGradeDao.findActualByPosition(p.getId());
            List<PositionKrLevelEntity> krLevels = positionKrLevelDao.getActualPositionKrLevelByUnitCodeAndPositionId(unitAccessService.getCurrentUnit(), p.getId());
            result.add(PositionFactory.createExtended(p, assignments, positionGrades, krLevels, divisionTeams, readiness));
        });

        return result;
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public DivisionTeamSuccessorDto createDivisionTeamSuccessor(Long divisionTeamRoleId, Long employeeId, Long employeeHrId, Date datePriority, Integer reasonIdInclusion, String commentInclusion, String documentUrlInclusion) throws NotFoundException {
        DivisionTeamRoleEntity divisionTeamRole = divisionTeamRoleDao.findById(divisionTeamRoleId)
                .orElseThrow(() -> new NotFoundException(String.format("Division team role with id=%d is not found", divisionTeamRoleId)));
        DivisionTeamEntity divisionTeam = divisionTeamRole.getDivisionTeam();

        if (checkEmployeeHrSuccessor(employeeHrId, employeeId, divisionTeam.getId()) || checkEmployeeHeadTeamDivisionSuccessor(employeeHrId, employeeId, divisionTeam.getId())) {
            EmployeeEntity employee = employeeDao.findById(employeeId)
                    .orElseThrow(() -> new NotFoundException(String.format("Employee with id=%d is not found", employeeId)));

            DivisionTeamSuccessorEntity divisionTeamSuccessorEntity = new DivisionTeamSuccessorEntity();
            divisionTeamSuccessorEntity.setDateFrom(new Date());
            divisionTeamSuccessorEntity.setDateTo(null);
            divisionTeamSuccessorEntity.setDateCommitHr(null);
            divisionTeamSuccessorEntity.setEmployee(employee);
            divisionTeamSuccessorEntity.setDivisionTeamRole(divisionTeamRole);
            divisionTeamSuccessorEntity.setDatePriority(datePriority);
            divisionTeamSuccessorEntity.setReasonIdInclusion(reasonIdInclusion);
            divisionTeamSuccessorEntity.setCommentInclusion(commentInclusion);
            divisionTeamSuccessorEntity.setDocumentUrlInclusion(documentUrlInclusion);
            divisionTeamSuccessorDao.save(divisionTeamSuccessorEntity);

            EmployeeEntity headTeamEmployee = employeeDao.findHeadTeamEmployee(divisionTeam.getId());
            if (headTeamEmployee != null) {
                DivisionTeamAssignmentEntity assignment = divisionTeamAssignmentDao.getCurrentDivisionTeamAssignmentByDivisionTeamRoleId(divisionTeamRoleId).get(0);
                notificationService.sendKafkaMessage(
                        new DataForKafkaMessageInputDto(
                                Collections.singletonList(headTeamEmployee.getId()),
                                String.format(MESSAGE_CREATE_DIVISION_TEAM_SUCCESSOR, assignment.getFullName()),
                                SUBJECT_CREATE_DIVISION_TEAM_SUCCESSOR)
                );
            }
            return DivisionTeamSuccessorFactory.create(divisionTeamSuccessorEntity);
        } else {
            throw new NotFoundException("Employee is not assigned as HR or is not head of team");
        }
    }

    @Override
    @Transactional(rollbackFor = {NotFoundException.class, AccessDeniedException.class})
    public Boolean deleteDivisionTeamSuccessor(Long divisionTeamSuccessorId, Long employeeHrId, Integer reasonIdExclusion, String commentExclusion, String documentUrlExclusion) throws NotFoundException, AccessDeniedException {
        DivisionTeamSuccessorEntity divisionTeamSuccessorEntity = divisionTeamSuccessorDao.findById(divisionTeamSuccessorId)
                .orElseThrow(() -> {
                    log.error("division_team_successor with id={} is not found", divisionTeamSuccessorId);
                    return new NotFoundException(String.format("division_team_successor with id=%d is not found", divisionTeamSuccessorId));
                });

        if (divisionTeamSuccessorEntity.getDateTo() != null) {
            log.error("division_team_successor with id={} is already closed", divisionTeamSuccessorId);
            throw new AccessDeniedException(String.format("division_team_successor with id=%d is already closed", divisionTeamSuccessorId));
        }

        Date currentDate = new Date();
        Long employeeId = divisionTeamSuccessorEntity.getEmployee().getId();
        if (checkEmployeeHrSuccessor(employeeHrId, employeeId, divisionTeamSuccessorEntity.getDivisionTeamRole().getDivisionTeam().getId())) {
            divisionTeamSuccessorEntity.setDateTo(currentDate);
            divisionTeamSuccessorEntity.setReasonIdExclusion(reasonIdExclusion);
            divisionTeamSuccessorEntity.setCommentExclusion(commentExclusion);
            divisionTeamSuccessorEntity.setDocumentUrlExclusion(documentUrlExclusion);
            divisionTeamSuccessorDao.save(divisionTeamSuccessorEntity);
            return true;
        } else {
            if (divisionTeamSuccessorEntity.getDateCommitHr() == null) {
                if (checkEmployeeHeadTeamDivisionSuccessor(employeeHrId, employeeId, divisionTeamSuccessorEntity.getDivisionTeamRole().getDivisionTeam().getId())) {
                    divisionTeamSuccessorEntity.setDateTo(currentDate);
                    divisionTeamSuccessorEntity.setReasonIdExclusion(reasonIdExclusion);
                    divisionTeamSuccessorEntity.setCommentExclusion(commentExclusion);
                    divisionTeamSuccessorEntity.setDocumentUrlExclusion(documentUrlExclusion);
                    divisionTeamSuccessorDao.save(divisionTeamSuccessorEntity);
                    return true;
                }
            } else {
                log.error("Only HR can remove a successor,  division team successor id = {}", divisionTeamSuccessorEntity.getId());
                throw new AccessDeniedException("Only HR can remove a successor");
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = {NotFoundException.class, AccessDeniedException.class})
    public DivisionTeamSuccessorReadinessDto createDivisionTeamSuccessorReadiness(Long divisionTeamSuccessorId, Integer assignmentReadinessId, Long employeeHrId) throws NotFoundException, AccessDeniedException {

        Optional<DivisionTeamSuccessorEntity> optionalDivisionTeamSuccessorEntity = divisionTeamSuccessorDao.findById(divisionTeamSuccessorId);
        if (optionalDivisionTeamSuccessorEntity.isEmpty()) {
            log.error("division team successor not found,  division team successor id = {}", divisionTeamSuccessorId);
            throw new NotFoundException("division team successor not found");
        }

        AssignmentReadinessEntity assignmentReadinessEntity = assignmentReadinessDao.findById(assignmentReadinessId)
            .orElseThrow(() -> {
                log.error("assignment readiness not found,  assignment readiness id = {}", assignmentReadinessId);
                return new NotFoundException("assignment readiness not found");
            });
        unitAccessService.checkUnitAccess(assignmentReadinessEntity.getUnitCode());

        DivisionTeamEntity divisionTeam = optionalDivisionTeamSuccessorEntity.get().getDivisionTeamRole().getDivisionTeam();
        if (checkEmployeeHr(employeeHrId, divisionTeam.getDivision().getId()) || checkEmployeeHeadTeamDivision(employeeHrId, divisionTeam.getId())) {

            List<DivisionTeamSuccessorReadinessEntity> divisionTeamSuccessorReadinessEntityList = divisionTeamSuccessorReadinessDao.findByDivisionTeamSuccessorId(divisionTeamSuccessorId);
            divisionTeamSuccessorReadinessEntityList.stream()
                    .filter(Objects::nonNull)
                    .filter(i -> Objects.isNull(i.getDateTo()))
                    .forEach(i -> {
                        i.setDateTo(new Date());
                        divisionTeamSuccessorReadinessDao.save(i);
                    });

            DivisionTeamSuccessorReadinessEntity divisionTeamSuccessorReadinessEntity = new DivisionTeamSuccessorReadinessEntity();
            divisionTeamSuccessorReadinessEntity.setDateFrom(new Date());
            divisionTeamSuccessorReadinessEntity.setDateTo(null);
            divisionTeamSuccessorReadinessEntity.setDivisionTeamSuccessor(optionalDivisionTeamSuccessorEntity.get());
            divisionTeamSuccessorReadinessEntity.setReadiness(assignmentReadinessEntity);
            divisionTeamSuccessorReadinessDao.save(divisionTeamSuccessorReadinessEntity);
            return DivisionTeamSuccessorReadinessFactory.create(divisionTeamSuccessorReadinessEntity);
        } else {
            log.error("Employee is not assigned as HR or is not head of team, employee id = {}", employeeHrId);
            throw new AccessDeniedException("Employee is not assigned as HR or is not head of team");
        }
    }

    @Override
    @Transactional(rollbackFor = {NotFoundException.class, AccessDeniedException.class})
    public DivisionTeamAssignmentRotationDto createDivisionTeamAssignmentRotation(Long divisionTeamAssignmentId, Integer assignmentRotationId, Long employeeHrId, Boolean techUser) throws NotFoundException, AccessDeniedException {
        Optional<DivisionTeamAssignmentEntity> optionalDivisionTeamAssignmentEntity = divisionTeamAssignmentDao.findById(divisionTeamAssignmentId);
        if (optionalDivisionTeamAssignmentEntity.isEmpty()) {
            log.error("division team assignment not found, division team assignment id = {}", divisionTeamAssignmentId);
            throw new NotFoundException("division team assignment not found");
        }
        List<PositionAssignmentEntity> positionAssignments = positionAssignmentDao.findAll(
                PositionAssignmentFilter.builder()
                    .employeeId(optionalDivisionTeamAssignmentEntity.get().getEmployee().getId())
                    .unitCode(unitAccessService.getCurrentUnit())
                    .build()
            );
        Long teamId = optionalDivisionTeamAssignmentEntity.get().getDivisionTeamRole().getDivisionTeam().getId();
        Long divisionId = optionalDivisionTeamAssignmentEntity.get().getDivisionTeamRole().getDivisionTeam().getDivision().getId();

        AssignmentRotationEntity optionalAssignmentRotationEntity = assignmentRotationDao.findById(assignmentRotationId)
            .orElseThrow(() -> {
                log.error("assignment rotation not found,  assignment rotation id = {}", assignmentRotationId);
                return new NotFoundException("assignment rotation not found");
            });
        unitAccessService.checkUnitAccess(optionalAssignmentRotationEntity.getUnitCode());

        if (techUser || checkEmployeeHr(employeeHrId, divisionId) || checkEmployeeHeadTeamDivision(employeeHrId, teamId)) {
            List<DivisionTeamAssignmentRotationEntity> listDivisionTeamAssignmentRotationByAssignment = divisionTeamAssignmentRotationDao.findByAssignmentId(divisionTeamAssignmentId);
            //            List<DivisionTeamAssignmentRotationEntity> listDivisionTeamAssignmentRotationByAssignmentAndRotation = dbContext.getDivisionTeamAssignmentRotationDataSource().findByAssignmentIdAndRotationId(divisionTeamAssignmentId, assignmentRotationId);

            closeAllExistingDivisionTeamAssignmentRotation(listDivisionTeamAssignmentRotationByAssignment);
            return DivisionTeamAssignmentRotationFactory.createWithJobInfo(createDivisionTeamAssignmentRotationEntity(
                    optionalDivisionTeamAssignmentEntity.get(),
                    optionalAssignmentRotationEntity,
                    employeeHrId,
                    divisionId
            ), positionAssignments);
        } else {
            log.error("Employee is not assigned as HR or is not head of team, employee id = {}", employeeHrId);
            throw new AccessDeniedException("Employee is not assigned as HR or is not head of team");
        }
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public void deletedDivisionTeamSuccessors() throws NotFoundException {
        List<DivisionTeamSuccessorEntity> divisionTeamSuccessors = divisionTeamSuccessorDao.getDivisionTeamSuccessorsForDeletion();
        if (divisionTeamSuccessors.isEmpty()) {
            throw new NotFoundException("Successors for deletion are not found");
        }

        Date currentDate = new Date();
        divisionTeamSuccessors.forEach(dts -> dts.setDateTo(currentDate));
        divisionTeamSuccessorDao.saveAll(divisionTeamSuccessors);
    }

    @Override
    @Transactional(rollbackFor = {NotFoundException.class, AccessDeniedException.class})
    public void commitTeamDivisionAssignmentRotation(Long divisionTeamAssignmentRotationId, Long employeeId) throws NotFoundException, AccessDeniedException {
        DivisionTeamAssignmentRotationEntity divisionTeamAssignmentRotation = divisionTeamAssignmentRotationService
                .getDivisionTeamAssignmentRotation(divisionTeamAssignmentRotationId);
        Long divisionId = divisionDao.findByDivisionTeamAssignmentRotation(divisionTeamAssignmentRotationId, unitAccessService.getCurrentUnit());
        //Long divisionId = divisionTeamAssignmentRotation.getAssignment().getDivisionTeamRole().getDivisionTeam().getDivision().getId();

        if (checkEmployeeHr(employeeId, divisionId)) {
            if (divisionTeamAssignmentRotation.getDateTo() == null && divisionTeamAssignmentRotation.getDateCommitHr() == null) {
                divisionTeamAssignmentRotation.setDateCommitHr(new Date());
                divisionTeamAssignmentRotationDao.save(divisionTeamAssignmentRotation);
            } else {
                throw new AccessDeniedException(String.format("division team assignment rotation %d is already closed or already commited", divisionTeamAssignmentRotation.getId()));
            }
        } else {
            throw new AccessDeniedException(String.format("employee %d is not assigned as HR", employeeId));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkEmployeeHeadTeamByAssignment(Long divisionTeamAssignmentId) {
        EmployeeTeamInfoDto employeeTeamInfoDto;
        if (divisionTeamAssignmentId != null) {
            employeeTeamInfoDto = divisionTeamAssignmentDao.findTeamInfoByAssignmentId(divisionTeamAssignmentId)
                    .orElseThrow(() -> {
                        log.error("division team assignment not found, division team assignment id = {}", divisionTeamAssignmentId);
                        return new NotFoundException(String.format("division team assignment with id=%d is not found", divisionTeamAssignmentId));
                    });
        } else {
            employeeTeamInfoDto = divisionTeamAssignmentDao.findFirstTeamInfoByExternalEmployeeId(authService.getCurrentUser().getEmployeeExternalId());
        }
        if (employeeTeamInfoDto == null) {
            return false;
        }
        return checkEmployeeHeadTeamDivision(employeeTeamInfoDto.getEmployeeId(), employeeTeamInfoDto.getTeamId());
    }

    @Override
    @Transactional(rollbackFor = {NotFoundException.class, AccessDeniedException.class})
    public void teamDivisionAssignmentRotationWithDraw(Long divisionTeamAssignmentRotationId, Long employeeId) throws NotFoundException, AccessDeniedException {
        DivisionTeamAssignmentRotationEntity divisionTeamAssignmentRotation = divisionTeamAssignmentRotationService.getDivisionTeamAssignmentRotation(divisionTeamAssignmentRotationId);
        Long divisionId = divisionDao.findByDivisionTeamAssignmentRotation(divisionTeamAssignmentRotationId, unitAccessService.getCurrentUnit());
        //Long divisionId = divisionTeamAssignmentRotation.getAssignment().getDivisionTeamRole().getDivisionTeam().getDivision().getId();

        if (checkEmployeeHr(employeeId, divisionId)) {
            if (divisionTeamAssignmentRotation.getDateTo() == null) {
                divisionTeamAssignmentRotation.setDateCommitHr(null);
                divisionTeamAssignmentRotationDao.save(divisionTeamAssignmentRotation);
            } else {
                throw new ForbiddenException("Field date_to not null");
            }
        } else {
            throw new AccessDeniedException(String.format("employee %d is not assigned as HR", employeeId));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DivisionTeamRoleContainerDto createRoleContainer(DivisionTeamRoleDto divisionTeamRole, boolean withPositionSuccessors) {
        DivisionTeamRoleContainerDto containerDto = new DivisionTeamRoleContainerDto();
        containerDto.setId(divisionTeamRole.getId());
        containerDto.setDivisionTeam(divisionTeamRole.getDivisionTeam());
        containerDto.setRole(divisionTeamRole.getRole());
        containerDto.setPositionImportance(divisionTeamRole.getPositionImportance());

        List<DivisionTeamAssignmentEntity> assignments =
                divisionTeamAssignmentDao.getDivisionTeamAssignmentsByDivisionTeamRoleId(divisionTeamRole.getId());

        containerDto.setDivisionTeamAssignmentDtos(assignmentService.getAssignmentsCreateShortWithJobInfo(assignments, Boolean.TRUE, Boolean.TRUE));

        List<DivisionTeamSuccessorEntity> divisionTeamSuccessors =
                divisionTeamSuccessorDao.getDivisionTeamSuccessorsByDivisionTeamRoleId(divisionTeamRole.getId());

        containerDto.setDivisionTeamSuccessorDtos(getDivisionTeamSuccessorsCreateShort(divisionTeamSuccessors, divisionTeamRole.getDivisionTeam()));

        if (withPositionSuccessors) {
            containerDto.setPositionSuccessorDtos(getPositionSuccessors(containerDto));
            containerDto.setPositionSuccessorReadinessDtos(getPositionSuccessorReadiness(containerDto));
        }
        return containerDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DivisionTeamRoleContainerDto> createRoleContainers(List<DivisionTeamRoleDto> divisionTeamRoles, boolean withPositionSuccessors) {
        List<DivisionTeamRoleContainerDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(divisionTeamRoles)) {
            List<Long> divisionTeamRoleIds = divisionTeamRoles.stream()
                    .map(DivisionTeamRoleDto::getId)
                    .collect(Collectors.toList());
            Map<Long, List<DivisionTeamAssignmentEntity>> assignments = divisionTeamAssignmentDao.getDivisionTeamAssignmentsByDivisionTeamRoleIds(divisionTeamRoleIds);
            Map<Long, List<DivisionTeamSuccessorEntity>> divisionTeamSuccessors = divisionTeamSuccessorDao.getDivisionTeamSuccessorsByDivisionTeamRoleIds(divisionTeamRoleIds);
            for (DivisionTeamRoleDto divisionTeamRole : divisionTeamRoles) {
                DivisionTeamRoleContainerDto containerDto = new DivisionTeamRoleContainerDto();
                containerDto.setId(divisionTeamRole.getId());
                containerDto.setDivisionTeam(divisionTeamRole.getDivisionTeam());
                containerDto.setRole(divisionTeamRole.getRole());
                containerDto.setPositionImportance(divisionTeamRole.getPositionImportance());

                List<DivisionTeamAssignmentEntity> assignmentEntities = assignments.getOrDefault(containerDto.getId(), new ArrayList<>());
                containerDto.setDivisionTeamAssignmentDtos(assignmentService.getAssignmentsCreateShortWithJobInfo(assignmentEntities, Boolean.TRUE, Boolean.TRUE));

                List<DivisionTeamSuccessorEntity> divisionTeamSuccessorEntities = divisionTeamSuccessors.getOrDefault(containerDto.getId(), new ArrayList<>());
                containerDto.setDivisionTeamSuccessorDtos(getDivisionTeamSuccessorsCreateShort(divisionTeamSuccessorEntities, divisionTeamRole.getDivisionTeam()));

                if (withPositionSuccessors) {
                    containerDto.setPositionSuccessorDtos(getPositionSuccessors(containerDto));
                    containerDto.setPositionSuccessorReadinessDtos(getPositionSuccessorReadiness(containerDto));
                }
                result.add(containerDto);
            }
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DivisionWithDivisionTeamsStatDto> teamAssignmentRotationStatDto(Long legalEntityId) {
        List<DivisionEntity> divisions = divisionDao.findAll(
            DivisionFilter.builder()
                .legalEntityId(legalEntityId)
                .unitCode(unitAccessService.getCurrentUnit())
                .build()
        );
        List<DivisionWithDivisionTeamsStatDto> divisionsWithDivisionTeams = new ArrayList<>();

        for (DivisionEntity division : divisions) {
            List<DivisionTeamEntity> divisionTeams = divisionTeamDao.findByDivisionId(division.getId());
            List<DivisionTeamStatDto> divisionTeamStats = new ArrayList<>();
            divisionTeams.forEach(divisionTeam -> {
                List<DivisionTeamAssignmentRotationEntity> assignmentRotations = divisionTeamAssignmentRotationDao.getAllByDivisionTeamId(divisionTeam.getId());
                int assignmentsCount = assignmentRotations.size();
                List<Integer> rotationIds = assignmentRotations
                        .stream()
                        .map(divisionTeamAssignmentRotation -> divisionTeamAssignmentRotation.getRotation().getId())
                        .collect(Collectors.toList());
                int rotationsCount = new HashSet<>(rotationIds).size();
                long approvedAssignmentsCount = assignmentRotations
                        .stream()
                        .filter(divisionTeamAssignmentRotation -> divisionTeamAssignmentRotation.getDateCommitHr() != null)
                        .count();
                Map<Integer, Integer> countMap = calculateRepeats(rotationIds);
                List<AssignmentRotationStatDto> rotations = new ArrayList<>();
                assignmentRotations.forEach(divisionTeamAssignmentRotation -> {
                                                Integer rotationId = divisionTeamAssignmentRotation.getRotation().getId();
                                                int rotationsCountForDivisionTeam = countMap.get(rotationId);
                                                rotations.add(new AssignmentRotationStatDto(rotationId, rotationsCountForDivisionTeam));
                                            }
                );
                divisionTeamStats.add(DivisionTeamFactory.createStat(divisionTeam, assignmentsCount, rotationsCount, approvedAssignmentsCount, rotations));
            });
            divisionsWithDivisionTeams.add(DivisionFactory.createWithDivisionTeamsStat(division, divisionTeamStats));
        }
        return divisionsWithDivisionTeams;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DivisionWithDivisionTeamsWithDivisionTeamRolesAndDivisionTeamSuccessorsDto> getDivisionTeamSuccessorStat(Long legalEntityId) {
        List<DivisionEntity> divisions = divisionDao.findAll(
            DivisionFilter.builder()
                .legalEntityId(legalEntityId)
                .unitCode(unitAccessService.getCurrentUnit())
                .build()
        );
        List<DivisionWithDivisionTeamsWithDivisionTeamRolesAndDivisionTeamSuccessorsDto> divisionsWithDivisionTeams = new ArrayList<>();
        for (DivisionEntity division : divisions) {
            List<DivisionTeamEntity> divisionTeams = divisionTeamDao.findByDivisionId(division.getId());
            List<DivisionTeamWithRolesAndSuccessorsStatDto> divisionTeamsStats = new ArrayList<>();
            for (DivisionTeamEntity divisionTeam : divisionTeams) {
                List<DivisionTeamRoleEntity> roles = divisionTeamRoleDao.findAllByDivisionTeam(divisionTeam.getId());
                float teamSecurity = 0f;
                int successorsCount = 0;
                int acceptedSuccessors = 0;
                Map<Integer, Integer> importanceCount = new HashMap<>();
                float totalReadinessStatus = 0;
                float totalSuccessorsStatus = 0;
                for (DivisionTeamRoleEntity role : roles) {
                    if (role.getPositionImportance() != null) {
                        Integer importanceId = role.getPositionImportance().getId();
                        importanceCount.put(importanceId, importanceCount.getOrDefault(importanceId, 0) + 1);
                    }

                    List<DivisionTeamSuccessorEntity> successors = divisionTeamSuccessorDao.
                            getDivisionTeamSuccessorsByDivisionTeamRoleId(role.getId());
                    successorsCount += successors.size();
                    for (DivisionTeamSuccessorEntity successor : successors) {
                        float readinessStatus = 0;
                        List<DivisionTeamSuccessorReadinessEntity> readiness = divisionTeamSuccessorReadinessDao.findByDivisionTeamSuccessorId(successor.getId());
                        if (!readiness.isEmpty()) {
                            switch (readiness.get(0).getReadiness().getId()) {
                                case READY:
                                    readinessStatus = 1;
                                    break;
                                case READY_IN_ONE_YEAR:
                                    readinessStatus = 0.5f;
                                    break;
                                case READY_IN_TWO_YEARS:
                                    readinessStatus = 0.25f;
                                    break;
                            }
                        }
                        totalReadinessStatus += readinessStatus;
                        if (successor.getDateCommitHr() != null) {
                            acceptedSuccessors++;
                            totalSuccessorsStatus += 1;
                        } else {
                            totalSuccessorsStatus += 0.75f;
                        }
                    }
                    if (successorsCount > 0) {
                        teamSecurity += (totalReadinessStatus / successorsCount) * (totalSuccessorsStatus / successorsCount);
                    }
                }

                List<DivisionTeamRoleAndImportanceStatDto> roleAndImportanceStat = new ArrayList<>();
                for (Map.Entry<Integer, Integer> entry : importanceCount.entrySet()) {
                    roleAndImportanceStat.add(new DivisionTeamRoleAndImportanceStatDto(new ImportanceStatDto(entry.getKey(), entry.getValue())));
                }
                DivisionTeamSuccessorsStatDto successorsStat = new DivisionTeamSuccessorsStatDto(successorsCount,
                                                                                                 acceptedSuccessors);
                divisionTeamsStats.add(
                        new DivisionTeamWithRolesAndSuccessorsStatDto(divisionTeam.getId(), roleAndImportanceStat,
                                                                      successorsStat, teamSecurity / roles.size()));

            }
            divisionsWithDivisionTeams.add(new DivisionWithDivisionTeamsWithDivisionTeamRolesAndDivisionTeamSuccessorsDto(
                    division.getId(), divisionTeamsStats
            ));
        }
        return divisionsWithDivisionTeams;
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public void divisionTeamSuccessorUpdateHr(Long divisionTeamSuccessorId, Date dateCommitHr) throws NotFoundException {
        DivisionTeamSuccessorEntity divisionTeamSuccessor = divisionTeamSuccessorDao
                .findById(divisionTeamSuccessorId)
                .orElseThrow(() -> new NotFoundException(String.format("Division team successor with id=%d is not found", divisionTeamSuccessorId)));
        divisionTeamSuccessor.setDateCommitHr(dateCommitHr);
        divisionTeamSuccessorDao.save(divisionTeamSuccessor);

        if (Objects.nonNull(dateCommitHr)) {
            var event6 = new SuccessorNotificateByCodeInputData("6", divisionTeamSuccessorId);
            notificationService.successorNotificateByCode(event6);

            var event7 = new SuccessorNotificateByCodeInputData("7", divisionTeamSuccessorId);
            notificationService.successorNotificateByCode(event7);

            var event8 = new SuccessorNotificateByCodeInputData("8", divisionTeamSuccessorId);
            notificationService.successorNotificateByCode(event8);
        } else {
            var event9 = new SuccessorNotificateByCodeInputData("9", divisionTeamSuccessorId);
            notificationService.successorNotificateByCode(event9);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DivisionTeamSuccessorDto getDivisionTeamSuccessor(Long id) throws NotFoundException {
        DivisionTeamSuccessorEntity entity = divisionTeamSuccessorDao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("Division team successor with id=%d is not found", id)));
        return DivisionTeamSuccessorFactory.create(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DivisionTeamAssignmentEntity> findAllDivisionTeamAssignment(DivisionTeamAssignmentFindRequestDto request, Pageable pageable) {
        if (request.isEmpty()) {
            throw new IllegalArgumentException("request cannot be null or empty");
        }
        return divisionTeamAssignmentDao.findAllByParams(request.getLegalEntityIds(), request.getDivisionIds(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DivisionTeamAssignmentEntity> findAllDivisionTeamAssignment(DivisionTeamAssignmentFindRequestDto request) {
        if (request.isEmpty()) {
            throw new IllegalArgumentException("request cannot be null or empty");
        }
        return divisionTeamAssignmentDao.findAllByParams(request.getLegalEntityIds(), request.getDivisionIds());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findAllDivisionTeamAssignmentIds(DivisionTeamAssignmentFindRequestDto request) {
        if (request.isEmpty()) {
            throw new IllegalArgumentException("request cannot be null or empty");
        }
        return divisionTeamAssignmentDao.findAllIdsByParams(request.getLegalEntityIds(), request.getDivisionIds());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findAllInLegalEntityIdsExceptAssignmentId(Long assignmentId, List<Long> legalEntityIds, Long employeeId) {
        if (legalEntityIds != null && !legalEntityIds.isEmpty()) {
            legalEntityIds = legalEntityDao.findByIds(legalEntityIds, unitAccessService.getCurrentUnit())
                .stream()
                .map(LegalEntityEntity::getId)
                .collect(Collectors.toList());
        } else {
            legalEntityIds = legalEntityDao.findAllByEmployeeId(employeeId, unitAccessService.getCurrentUnit());
        }
        return divisionTeamAssignmentDao.findAllInLegalEntityIdsExceptAssignmentId(assignmentId, legalEntityIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DivisionGroupEntity> getDivisionGroupEntitiesByIds(List<Long> ids) {
        return divisionGroupDao.findActualByIds(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DivisionGroupEntity> getAllDivisionGroupEntities() {
        return divisionGroupDao.findAllActual(Pageable.unpaged()).getContent();
    }

    @Override
    public DivisionInfo getDivisionInfo(Long divisionId) {
        log.debug("Get division info for division: {}", divisionId);
        Optional<DivisionEntity> division = divisionDao.findById(divisionId);
        if (division.isEmpty()) {
            log.debug("Division {} does not exists", divisionId);
            return new DivisionInfo(null, null, null);
        }
        unitAccessService.checkUnitAccess(division.get().getLegalEntityEntity().getUnitCode());
        List<DivisionTeamAssignmentEntity> divisionTeamAssignments = divisionTeamAssignmentDao
                .getHeadDivisionTeamAssignmentByDivisionId(divisionId,
                                                           GlobalDefs.HEAD_ID, GlobalDefs.HEAD_SYSTEM_ROLE_ID);
        if (divisionTeamAssignments.isEmpty()) {
            log.debug("Division team assignment (with head role) for {} division does not exists", divisionId);
            return new DivisionInfo(division.get(), null, null);
        }
        return new DivisionInfo(division.get(),
                                divisionTeamAssignments.get(0).getEmployee(),
                                employeeService.getPositionAssignmentInfo(divisionTeamAssignments.get(0).getEmployee().getId()));
    }

    @Override
    public DivisionInfoDto getDivisionInfoDto(Long divisionId) {
        DivisionInfo info = getDivisionInfo(divisionId);
        List<DivisionTeamEntity> divisionTeams =
                info.getDivision() != null ? divisionTeamDao.findByDivisionId(info.getDivision().getId()) : new ArrayList<>();

        return DivisionInfoFactory.create(
                info.getDivision(),
                info.getDivisionHead(),
                info.getDivisionHeadInfo() == null ? null : info.getDivisionHeadInfo().getAssignments(),
                divisionTeams
        );
    }

    @Override
    public List<DivisionInfoDto> getDivisionInfoByParams(List<Long> divisionIds, Long parentId, Long legalEntityId, List<Long> groupIds, boolean withChilds) {
        List<DivisionEntity> divisions = divisionDao.findAll(
            DivisionFilter.builder()
                .divisionIds(divisionIds)
                .parentIds(List.of(parentId))
                .legalEntityId(legalEntityId)
                .groupIds(groupIds)
                .unitCode(unitAccessService.getCurrentUnit())
                .build()
        );
        if (withChilds) {
            recursionFillDivisions(divisions, divisions);
        }
        return getDivisionInfoDtoList(divisions);
    }

    @Override
    public DivisionPathData getDivisionPathInfo(Long divisionId) {
        log.debug("Get division path for division: {}", divisionId);
        DivisionPathData path = new DivisionPathData();
        path.setCurrent(getDivisionInfo(divisionId));
        if (path.getCurrent().getDivision() == null || path.getCurrent().getDivision().getParentId() == null) {
            log.debug("Current division ({}) or division parent is null", divisionId);
            return path;
        }
        List<DivisionShortInfo> parents = new ArrayList<>();
        Optional<DivisionEntity> division = divisionDao.findById(path.getCurrent().getDivision().getParentId());
        while (division.isPresent()) {
            DivisionEntity divisionEntity = division.get();
            unitAccessService.checkUnitAccess(divisionEntity.getLegalEntityEntity().getUnitCode());
            if (parents.stream().anyMatch(item -> Objects.equals(item.getId(), divisionEntity.getId()))) {
                log.error("An loop occurred inside getting division path for division {}", divisionEntity.getId());
                throw new RuntimeException("Loop inside divisions");
            }
            // todo: check that there is no loop
            parents.add(new DivisionShortInfo(divisionEntity.getId(),
                                              divisionEntity.getParentId(),
                                              divisionEntity.getFullName(), divisionEntity.getShortName(), divisionEntity.getAbbreviation()));
            if (divisionEntity.getParentId() == null) {
                break;
            }
            division = divisionDao.findById(division.get().getParentId());
            unitAccessService.checkUnitAccess(divisionEntity.getLegalEntityEntity().getUnitCode());
        }
        path.setParents(parents);
        return path;
    }

    @Override
    public List<DivisionShortInfo> getPath(Long id) {
        log.debug("Get division full path for division: {}", id);
        DivisionPathData path = getDivisionPathInfo(id);
        List<DivisionShortInfo> info = path.getParents() != null ? path.getParents() : new ArrayList<>();
        DivisionEntity division = path.getCurrent().getDivision();
        if (division != null) {
            info.add(0, new DivisionShortInfo(division.getId(), division.getParentId(),
                                              division.getFullName(), division.getShortName(),
                                              division.getAbbreviation()));
        }
        return info;
    }

    @Override
    public boolean checkEmployeeHeadTeamDivision(Long employeeId, Long teamId) {
        log.debug("checking employee head team for employee: {} and division team: {}", employeeId, teamId);
        Set<Long> employeeDivisionIds = divisionTeamAssignmentDao.getDivisionIdsByEmployeeIdAndSystemRoleId(employeeId, 1);
        if (!employeeDivisionIds.isEmpty()) {
            Optional<DivisionTeamEntity> divisionTeam = divisionTeamDao.findById(teamId);
            if (divisionTeam.isEmpty()) {
                throw new NotFoundException(String.format("division team %d is not found", teamId));
            }
            DivisionEntity division = divisionDao.find(
                DivisionFilter.builder()
                    .divisionTeamId(teamId)
                    .unitCode(unitAccessService.getCurrentUnit())
                    .build());
            if (division == null) {
                throw new NotFoundException(String.format("division with division team id = %d for current unit is not found", teamId));
            }
            Long currentTeamDivisionId = division.getId();
            List<Long> parents = divisionLinksDao.findAllParents(currentTeamDivisionId);

            if (CollectionUtils.containsAny(employeeDivisionIds, parents)) {
                log.debug("employee {} is head of team or head of parent level", employeeId);
                return true;
            }
            log.error("employee {} is not head of team and all parent levels", employeeId);
            return false;
        }
        log.error("employee {} is not head of team and all parent levels", employeeId);
        return false;
    }

    @Override
    public boolean checkEmployeeHeadTeamDivisionSuccessor(Long employeeHeadId, Long employeeId, Long teamId) throws NotFoundException {
        log.debug("checking employee head team successor for employee: {} and division team: {}", employeeHeadId, teamId);
        Set<Long> employeeDivisionIds = divisionTeamAssignmentDao.getDivisionIdsByEmployeeIdAndSystemRoleId(employeeHeadId, 1);
        if (!employeeDivisionIds.isEmpty()) {
            Optional<DivisionTeamEntity> divisionTeam = divisionTeamDao.findById(teamId);
            if (divisionTeam.isEmpty()) {
                throw new NotFoundException(String.format("division team %d is not found", teamId));
            }

            List<DivisionTeamAssignmentShortDto> divisionTeamSubordinates = assignmentService.getDivisionTeamSubordinates(employeeHeadId, null, teamId, true);
            if (divisionTeamSubordinates.stream().noneMatch(divisionTeamAssignmentDto -> divisionTeamAssignmentDto.getEmployee().getId().equals(employeeId))) {
                log.error("employee {} is not head of team {} successor subordinate with employee {}", employeeHeadId, teamId, employeeId);
                return false;
            }

            DivisionEntity division = divisionDao.find(
                DivisionFilter.builder()
                    .divisionTeamId(teamId)
                    .build());
            Long currentTeamDivisionId = division.getId();
            List<Long> parents = divisionLinksDao.findAllParents(currentTeamDivisionId);

            if (CollectionUtils.containsAny(employeeDivisionIds, parents)) {
                    log.debug("employee {} is head of team successor or head of parent level", employeeHeadId);
                    return true;
            }
            log.error("employee {} is not head of team successor and all parent levels", employeeHeadId);
            return false;
        }
        log.error("system role id=1 is not found for employee {}", employeeHeadId);
        return false;
    }

    @Override
    public boolean checkEmployeeHr(Long employeeId, Long divisionId) throws NotFoundException {
        log.debug("checking hr employee for employee: {} and division: {}", employeeId, divisionId);
        Long legal = legalEntityDao.findIdByDivisionId(divisionId, unitAccessService.getCurrentUnit());
        List<Integer> systemRoleIds = roleDao.findSystemRoleIdsByEmployeeIdAndLegalEntityId(employeeId, legal);

        if (systemRoleIds.isEmpty()) {
            log.error("employee {} is not hr of division {}", employeeId, divisionId);
            return false;
        }
        if (systemRoleIds.stream().noneMatch(GlobalDefs.HR_ROLE_SET::contains)) {
            log.error("employee {} is not hr of division {}", employeeId, divisionId);
            return false;
        }
        log.debug("employee {} is hr of division {}", employeeId, divisionId);
        return true;
    }

    @Override
    public boolean checkEmployeeHrSuccessor(Long employeeHrId, Long employeeId, Long teamId) {
        log.debug("checking hr employee for employee: {} and employee hr: {}", employeeId, employeeHrId);
        List<Long> list = legalEntityDao.findAllByEmployeeRole(employeeHrId, GlobalDefs.HR_ROLE_SET, unitAccessService.getCurrentUnit());
        Set<Long> employeeHrLegalEntityIds = new HashSet<>(list);
        if (employeeHrLegalEntityIds.isEmpty()) {
            log.error("employee {} has no legal entities with HR roles", employeeHrId);
            return false;
        }
        final Long teamLegalEntityId = legalEntityDao.findIdByDivisionTeam(teamId, unitAccessService.getCurrentUnit());
        if (teamLegalEntityId == null) {
            throw new RuntimeException(String.format("division team %d is not found", teamId));
        }
        if (employeeHrLegalEntityIds.stream().noneMatch(legalEntityIds -> legalEntityIds.equals(teamLegalEntityId))) {
            log.error("employee {} has no division team {}", employeeHrId, teamId);
            return false;
        }
        List<Long> employeeLegalEntityIds = legalEntityDao.findAllByEmployeeId(employeeId, unitAccessService.getCurrentUnit());
        if (employeeLegalEntityIds.stream().noneMatch(employeeHrLegalEntityIds::contains)) {
            log.error("employee {} is not hr of employee hr {} by legal entities {} and {}", employeeHrId, employeeId, employeeHrLegalEntityIds, employeeLegalEntityIds);
            return false;
        }
        log.debug("employee {} is hr of employee hr {}", employeeHrId, employeeId);
        return true;
    }

    @Override
    public void rebuildDivisionTree() {
        divisionDao.deleteAll();
        nativeDao.rebuildDivisionTree();
    }

    @Override
    @Transactional(readOnly = true)
    public DivisionTeamEntity getDivisionTeamByTypeAndEmployeeId(Integer divisionTeamTypeId, Long employeeId) {
        List<DivisionTeamAssignmentEntity> assignments = divisionTeamAssignmentDao.findByEmployeeId(employeeId);
        DivisionTeamEntity team = null;
        for (DivisionTeamAssignmentEntity assignment : assignments) {
            if (Objects.equals(assignment.getDivisionTeamRole().getRole().getSystemRole().getId(), GlobalDefs.HEAD_SYSTEM_ROLE_ID)) {
                team = assignment.getDivisionTeamRole().getDivisionTeam();
                break;
            }
        }
        if (team == null) {
            team = assignments.get(0).getDivisionTeamRole().getDivisionTeam();
        }
        if (Objects.equals(team.getType().getId(), divisionTeamTypeId)) {
            return team;
        } else if (team.getParent() != null) {
            return getDivisionTeamByTypeAndEmployeeId(divisionTeamTypeId, employeeId);
        } else {
            throw new NotFoundException(String.format("No division team has been found for employee %d and division team type %d",
                                                    employeeId, divisionTeamTypeId));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DivisionTeamDto findDivisionTeamById(Long id) {
        return DivisionTeamFactory.create(findDivisionTeamEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DivisionTeamDto> findActual() {
        return divisionTeamDao.findActual()
                .stream()
                .map(DivisionTeamFactory::create)
                .collect(Collectors.toList());
    }

    public List<DivisionDto> findPost(DivisionFindDto dto) {
        return divisionDao.findPost(dto.getId(), dto.getLegalEntityId(), dto.getPage(), dto.getSize(), dto.getSearchingValue(),
            dto.getWithClosed(), unitAccessService.getCurrentUnit()
            ).stream()
            .map(DivisionFactory::create)
            .collect(Collectors.toList());
    }

    @Override
    public DivisionTeamAssignmentDto getTeamDivisionHeadHead(Long employeeId, String externalId, Long divisionTeamId) {
        Long id = getEmployeeId(employeeId, externalId);
        EmployeeSearchResult headSearchResult = employeeService.getDivisionTeamHead(id, divisionTeamId);
        if (!headSearchResult.getSearchStatus()) {
            throw new NotFoundException("search result is empty, therefore we could't not return info");
        }
        if (headSearchResult.getEmployee() == null) {
            throw new NotFoundException("employee head not found, therefore we could't not return info");
        }
        EmployeeSearchResult headHeadSearchResult = employeeService.getDivisionTeamHead(headSearchResult.getEmployee().getId(), headSearchResult.getDivisionTeamId());
        if (headHeadSearchResult.getEmployee() == null) {
            throw new NotFoundException("head employee head not found, therefore we could't not return info");
        }
        DivisionTeamAssignmentEntity assignment = divisionTeamAssignmentDao.findById(headHeadSearchResult.getDivisionTeamAssignmentId()).get();
        List<DivisionTeamAssignmentRotationShortDto> rotations = divisionTeamAssignmentRotationDao
            .findByAssignmentId(assignment.getId())
            .stream()
            .map(DivisionTeamAssignmentRotationFactory::createShort)
            .collect(Collectors.toList());
        List<PositionAssignmentEntity> positionAssignments = positionAssignmentDao.findAll(
            PositionAssignmentFilter.builder()
                .unitCode(unitAccessService.getCurrentUnit())
                .employeeId(assignment.getEmployeeId())
                .build()
        );
        return DivisionTeamAssignmentFactory.createWithJobInfo(assignment, positionAssignments, rotations);
    }

    private Long getEmployeeId(Long id, String externalId) {
        if (id != null) {
            return id;
        }
        if (externalId != null) {
            return employeeService.getEmployeeIdByExternalId(externalId);
        }
        return authService.getUserEmployeeId();
    }

    private DivisionTeamEntity findDivisionTeamEntityById(Long id) {
        return divisionTeamDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("division_team with id=%d is not found", id) ));
    }

    private void recursionFillDivisions(List<DivisionEntity> parentList, List<DivisionEntity> divisions) {
        if (!parentList.isEmpty()) {
            List<DivisionEntity> divisionsByParentsIds =
                    divisionDao.findAll(
                        DivisionFilter.builder()
                            .parentIds(parentList.stream().map(DivisionEntity::getId).collect(Collectors.toList()))
                            .unitCode(unitAccessService.getCurrentUnit())
                            .build()
                    );
            divisions.addAll(divisionsByParentsIds);
            recursionFillDivisions(divisionsByParentsIds, divisions);
        }
    }

    private List<DivisionInfoDto> getDivisionInfoDtoList(List<DivisionEntity> divisions) {
        List<Long> divisionIds = divisions.stream().map(DivisionEntity::getId).collect(Collectors.toList());
        final Map<Long, List<DivisionTeamAssignmentEntity>> divisionTeamAssignmentsMap =
                divisionTeamAssignmentDao.findHeadByDivisionIds(divisionIds, GlobalDefs.HEAD_ID, GlobalDefs.HEAD_SYSTEM_ROLE_ID);

        final Map<Long, List<DivisionTeamEntity>> divisionTeamsByDivisionIds = divisionTeamDao.findActualByDivisionIds(divisionIds);

        List<Long> employeeIds = new ArrayList<>();
        divisionTeamAssignmentsMap.forEach((divId, dtaList) -> {
            if (!dtaList.isEmpty()) {
                employeeIds.add(dtaList.get(0).getEmployee().getId());
            }
        });
        final Map<Long, List<PositionAssignmentEntity>> positionAssignmentsByEmployeeIds =
            positionAssignmentDao.findActualByEmployeeIds(employeeIds, unitAccessService.getCurrentUnit());
        return divisions
                .stream()
                .map(d -> {
                    List<DivisionTeamAssignmentEntity> assignments = divisionTeamAssignmentsMap.getOrDefault(d.getId(), Collections.emptyList());
                    return DivisionInfoFactory.create(
                            d,
                            assignments.isEmpty() ? null : assignments.get(0).getEmployee(),
                            assignments.isEmpty() ? null : positionAssignmentsByEmployeeIds.getOrDefault(assignments.get(0).getEmployee().getId(), Collections.emptyList()),
                            divisionTeamsByDivisionIds.getOrDefault(d.getId(), Collections.emptyList())
                    );
                })
                .collect(Collectors.toList());
    }

    private Map<Integer, Integer> calculateRepeats(List<Integer> list) {
        Map<Integer, Integer> result = new HashMap<>();
        if (list.isEmpty()) {
            return result;
        }
        list.forEach(x -> {
                         if (result.containsKey(x)) {
                             int value = result.get(x);
                             value += 1;
                             result.put(x, value);
                         } else {
                             result.put(x, 1);
                         }
                     }
        );
        return result;
    }

    private void closeAllExistingDivisionTeamAssignmentRotation(List<DivisionTeamAssignmentRotationEntity> listByAssignment) {
        listByAssignment.stream()
                .filter(Objects::nonNull)
                .filter(i -> Objects.isNull(i.getDateTo()))
                .forEach(i -> {
                    i.setDateTo(new Date());
                    divisionTeamAssignmentRotationDao.save(i);
                });
    }

    private DivisionTeamAssignmentRotationEntity createDivisionTeamAssignmentRotationEntity(DivisionTeamAssignmentEntity dta, AssignmentRotationEntity assignmentRotation, Long employeeHrId, Long divisionId) throws NotFoundException {
        DivisionTeamAssignmentRotationEntity divisionTeamAssignmentRotationEntity = new DivisionTeamAssignmentRotationEntity();
        divisionTeamAssignmentRotationEntity.setDateFrom(new Date());
        divisionTeamAssignmentRotationEntity.setDateTo(null);
        if (checkEmployeeHr(employeeHrId, divisionId)) {
            divisionTeamAssignmentRotationEntity.setDateCommitHr(new Date());
        } else {
            divisionTeamAssignmentRotationEntity.setDateCommitHr(null);
        }
        DivisionTeamAssignmentShort dtaShort = divisionTeamAssignmentDao.findShortById(dta.getId());
        divisionTeamAssignmentRotationEntity.setAssignment(dtaShort);
        unitAccessService.checkUnitAccess(assignmentRotation.getUnitCode());
        divisionTeamAssignmentRotationEntity.setRotation(assignmentRotation);
        divisionTeamAssignmentRotationDao.save(divisionTeamAssignmentRotationEntity);

        notificationService.sendKafkaMessage(
                new DataForKafkaMessageInputDto(
                        Collections.singletonList(dtaShort.getEmployeeId()),
                        MESSAGE_CREATE_DIVISION_TEAM_ASSIGNMENT_ROTATION,
                        SUBJECT_CREATE_DIVISION_TEAM_ASSIGNMENT_ROTATION)
        );

        return divisionTeamAssignmentRotationEntity;
    }

    private List<DivisionTeamSuccessorShortDto> getDivisionTeamSuccessorsCreateShort(List<DivisionTeamSuccessorEntity> divisionTeamSuccessors, DivisionTeamDto divisionTeam) {
        List<Long> divisionTeamSuccessorIds = divisionTeamSuccessors.stream().map(DivisionTeamSuccessorEntity::getId).collect(Collectors.toList());

        Map<Long, List<DivisionTeamSuccessorReadinessEntity>> successorsReadiness =
                divisionTeamSuccessorReadinessDao.findActualByDivisionTeamSuccessorIds(divisionTeamSuccessorIds);

        return divisionTeamSuccessors
                .stream()
                .map(dts ->
                             DivisionTeamSuccessorFactory.createShort(
                                     dts,
                                     successorsReadiness.getOrDefault(dts.getId(), Collections.emptyList())
                                             .stream()
                                             .map(DivisionTeamSuccessorReadinessFactory::create)
                                             .collect(Collectors.toList()), divisionTeam
                             )
                )
                .collect(Collectors.toList());
    }

    private List<PositionSuccessorDto> getPositionSuccessors(DivisionTeamRoleContainerDto containerDto) {
        List<Long> dtaIds = containerDto.getDivisionTeamAssignmentDtos().stream().map(DivisionTeamAssignmentShortDto::getId).collect(Collectors.toList());

        List<PositionSuccessorEntity> successors = positionDao.getPositionByDivisionTeamAssignments(dtaIds, unitAccessService.getCurrentUnit());
        if (successors.isEmpty()) {
            return new ArrayList<>();
        }

        return successors.stream()
            .map(PositionSuccessorFactory::create)
            .collect(Collectors.toList());
    }

    private List<PositionSuccessorReadinessDto> getPositionSuccessorReadiness(DivisionTeamRoleContainerDto containerDto) {
        List<Long> positionSuccessorIds = containerDto.getPositionSuccessorDtos()
            .stream().map(PositionSuccessorDto::getId)
            .collect(Collectors.toList());

        List<PositionSuccessorReadinessEntity> list = positionSuccessorReadinessDao.getByPositionSuccessorIds(positionSuccessorIds, unitAccessService.getCurrentUnit());
        return list.stream()
            .map(PositionSuccessorReadinessFactory::create)
            .collect(Collectors.toList());
    }
}
