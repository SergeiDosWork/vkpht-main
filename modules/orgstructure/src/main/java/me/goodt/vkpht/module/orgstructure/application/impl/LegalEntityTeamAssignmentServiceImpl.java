package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamAssignmentFilterDto;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import com.goodt.drive.rtcore.configuration.properties.AppProperties;
import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.common.application.util.TextConstants;
import me.goodt.vkpht.common.domain.dao.filter.LegalEntityFilter;
import me.goodt.vkpht.module.orgstructure.api.AssignmentService;
import me.goodt.vkpht.module.orgstructure.api.EmployeeService;
import me.goodt.vkpht.module.orgstructure.api.LegalEntityTeamAssignmentService;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.LegalEntityDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.LegalEntityTeamAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.RoleDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.RoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.factory.LegalEntityTeamAssignmentFactory;

import static java.util.stream.Collectors.toList;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_USER_TYPE_EMPLOYEE;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_USER_TYPE_TEAM_DIVISION_ASSIGNMENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class LegalEntityTeamAssignmentServiceImpl implements LegalEntityTeamAssignmentService {

    private final LegalEntityTeamAssignmentDao legalEntityTeamAssignmentDao;
    private final LegalEntityDao legalEntityDao;
    private final RoleDao roleDao;
    private final AppProperties appProperties;
    private final EmployeeService employeeService;
    private final AssignmentService assignmentService;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;

    private final Map<String, Function<LegalEntityTeamAssignmentFilterDto, List<LegalEntityTeamAssignmentDto>>> operationMode = Map.of(
        TextConstants.DB_TABLE_MODE, handleDbTable(),
        TextConstants.TOKEN_MODE, handleToken()
    );

    @Override
    public List<LegalEntityTeamAssignmentEntity> getLegalEntityTeamAssignments(Long employeeId) {
        return legalEntityTeamAssignmentDao.findByEmployeeId(employeeId);
    }

    @Override
    public List<LegalEntityTeamAssignmentDto> getLegalEntityTeamAssignments(Long legalEntityTeamAssignmentId, Long employeeId, String externalEmployeeId, Long legalEntityTeamId) {
        StopWatch global = StopWatch.createStarted();

        List<LegalEntityTeamAssignmentDto> assignmentsDto = new ArrayList<>();
        Function<LegalEntityTeamAssignmentFilterDto, List<LegalEntityTeamAssignmentDto>> function = operationMode.get(appProperties.getRoleMode());

        if (function != null) {
            assignmentsDto = function.apply(new LegalEntityTeamAssignmentFilterDto(legalEntityTeamAssignmentId, employeeId, externalEmployeeId, legalEntityTeamId));
        }
        global.stop();
        log.info("Method getLegalEntityTeamAssignments. Time {}", global.getTime());
        return assignmentsDto;
    }

    @Override
    public List<LegalEntityTeamAssignmentDto> getParentsLegalEntityTeamAssignments(Long employeeId, Long taskOwnerId, Long userTypeId) {
        StopWatch global = StopWatch.createStarted();

        List<LegalEntityTeamAssignmentEntity> employeeAssignments = getLegalEntityTeamAssignments(employeeId);

        List<DivisionTeamAssignmentDto> taskOwnerAssignments = Collections.emptyList();
        if (TASK_USER_TYPE_EMPLOYEE.equals(userTypeId)) {
            taskOwnerAssignments = assignmentService.
                getDivisionTeamAssignments(Collections.emptyList(), Collections.singletonList(taskOwnerId), null, false);
        } else if (TASK_USER_TYPE_TEAM_DIVISION_ASSIGNMENT.equals(userTypeId)) {
            taskOwnerAssignments = assignmentService.
                getDivisionTeamAssignments(Collections.singletonList(taskOwnerId), Collections.emptyList(), null, false);
        }

        Set<Long> taskOwnerDivisionIds = taskOwnerAssignments
            .stream()
            .map(assignment -> assignment.getDivisionTeamRole().getDivisionTeam().getDivisionId())
            .collect(Collectors.toSet());

        Set<Long> parentsIds = legalEntityDao.findByDivisionIds(taskOwnerDivisionIds, unitAccessService.getCurrentUnit())
            .stream()
            .map(le -> le.getParent().getId())
            .collect(Collectors.toSet());
        Set<Long> allParentsIds = getAllParentLegalEntityIds(parentsIds, new ArrayList<>(parentsIds));

        global.stop();
        log.info("Method getParentsLegalEntityTeamAssignments. Time {}", global.getTime());

        return employeeAssignments
            .stream()
            .filter(assignment -> allParentsIds.contains(assignment.getLegalEntityTeamEntity().getLegalEntityId()))
            .map(LegalEntityTeamAssignmentFactory::create)
            .collect(Collectors.toList());
    }

    private Set<Long> getAllParentLegalEntityIds(Set<Long> resultCollection, List<Long> parentIds) {
        if (parentIds.isEmpty()) {
            return resultCollection;
        }
        parentIds = legalEntityDao.findByIds(parentIds, unitAccessService.getCurrentUnit())
            .stream()
            .filter(le -> le.getParent() != null)
            .map(le -> le.getParent().getId())
            .collect(Collectors.toList());
        resultCollection.addAll(parentIds);
        return getAllParentLegalEntityIds(resultCollection, parentIds);
    }

    private Function<LegalEntityTeamAssignmentFilterDto, List<LegalEntityTeamAssignmentDto>> handleDbTable() {
        return dto -> getLegalEntityTeamAssignmentsByFilter(dto)
            .stream()
            .filter(item -> (dto.getLegalEntityTeamId() == null || Objects.equals(item.getLegalEntityTeamId(), dto.getLegalEntityTeamId())))
            .map(LegalEntityTeamAssignmentFactory::create)
            .collect(toList());
    }

    private Function<LegalEntityTeamAssignmentFilterDto, List<LegalEntityTeamAssignmentDto>> handleToken() {
        return dto -> {
            List<LegalEntityTeamAssignmentDto> assignmentsDto = new ArrayList<>();
            Optional<EmployeeEntity> employeeOptional = employeeService.getEmployee(dto.getEmployeeId(), dto.getExternalEmployeeId());
            List<String> groups = authService.getCurrentUser().getGroups();
            if (employeeOptional.isPresent() && groups != null) {
                if (appProperties.getIncludedDelimiter()) {
                    getAssignmentsWithIncludedDelimiter(assignmentsDto, groups, employeeOptional.get());
                } else {
                    groups = parseGroups(groups);
                    getAssignmentsByGroups(assignmentsDto, groups, employeeOptional.get());
                }
            }
            return assignmentsDto;
        };
    }

    private List<LegalEntityTeamAssignmentEntity> getLegalEntityTeamAssignmentsByFilter(LegalEntityTeamAssignmentFilterDto dto) {
        List<LegalEntityTeamAssignmentEntity> assignments = new ArrayList<>();
        if (dto.getLegalEntityTeamAssignmentId() != null) {
            Optional<LegalEntityTeamAssignmentEntity> assignmentEntity = legalEntityTeamAssignmentDao.findByIdWithFetch(dto.getLegalEntityTeamAssignmentId());
            if (assignmentEntity.isPresent()) {
                LegalEntityTeamAssignmentEntity assignment = assignmentEntity.get();
                if (dto.getEmployeeId() != null) {
                    if (Objects.equals(assignment.getEmployeeId(), dto.getEmployeeId())) {
                        if (dto.getLegalEntityTeamId() != null) {
                            if (Objects.equals(assignment.getLegalEntityTeamId(), dto.getLegalEntityTeamId())) {
                                assignments.add(assignment);
                            }
                        } else {
                            assignments.add(assignment);
                        }
                    }
                } else {
                    assignments.add(assignment);
                }
            }
        } else {
            Long employeeId = dto.getEmployeeId();
            if (employeeId == null)
                if (dto.getExternalEmployeeId() != null) {
                    employeeId = employeeService.getEmployeeIdByExternalId(dto.getExternalEmployeeId());
                } else {
                    employeeId = authService.getUserEmployeeId();
                }
            if (employeeId != null) {
                assignments = legalEntityTeamAssignmentDao.findByEmployeeId(employeeId);
            }
        }
        return assignments;
    }

    private List<String> parseGroups(List<String> groups) {
        groups = groups
            .stream()
            .map(group -> {
                int countSeparates = StringUtils.countMatches(group, TextConstants.SEPARATOR_DASH);
                if (countSeparates == 2) {
                    return StringUtils.substringBeforeLast(group, TextConstants.SEPARATOR_DASH);
                }
                return group;
            })
            .collect(toList());
        return groups;
    }

    private void getAssignmentsWithIncludedDelimiter(List<LegalEntityTeamAssignmentDto> assignmentsDto, List<String> groups, EmployeeEntity employee) {
        List<String> groupsWithoutSeparate = new ArrayList<>();
        groups.forEach(group -> {
            int countSeparates = StringUtils.countMatches(group, TextConstants.SEPARATOR_DASH);
            if (countSeparates == 2) {
                String legalEntityExternalId = StringUtils.substringAfterLast(group, TextConstants.SEPARATOR_DASH).substring(0, 4);
                group = StringUtils.substringBeforeLast(group, TextConstants.SEPARATOR_DASH);
                List<LegalEntityEntity> legalEntityEntityList = legalEntityDao.find(
                    LegalEntityFilter.builder()
                        .externalId(legalEntityExternalId)
                        .unitCode(unitAccessService.getCurrentUnit())
                        .build()
                );
                if (!legalEntityEntityList.isEmpty()) {
                    List<RoleEntity> roles = roleDao.findByCode(group);
                    createAndAddAssignments(assignmentsDto, employee, legalEntityEntityList, roles);
                }
            } else {
                groupsWithoutSeparate.add(group);
            }
        });
        if (!groupsWithoutSeparate.isEmpty()) {
            getAssignmentsByGroups(assignmentsDto, groupsWithoutSeparate, employee);
        }
    }

    private void getAssignmentsByGroups(List<LegalEntityTeamAssignmentDto> assignmentsDto, List<String> groups, EmployeeEntity employee) {
        List<LegalEntityEntity> legalEntityEntityList = legalEntityDao.find(LegalEntityFilter.asDefault());
        if (!legalEntityEntityList.isEmpty()) {
            List<RoleEntity> roles = roleDao.findByCodes(groups);
            if (CollectionUtils.isNotEmpty(roles)) {
                createAndAddAssignments(assignmentsDto, employee, legalEntityEntityList, roles);
            }
        }
    }

    private void createAndAddAssignments(List<LegalEntityTeamAssignmentDto> assignmentsDto, EmployeeEntity employee, List<LegalEntityEntity> legalEntityEntityList, List<RoleEntity> roles) {
        for (LegalEntityEntity legalEntityEntity : legalEntityEntityList) {
            assignmentsDto.addAll(roles.stream()
                .map(r -> LegalEntityTeamAssignmentFactory.create(new LegalEntityTeamAssignmentEntity(
                    null, new Date(), null, null, employee,
                    new LegalEntityTeamEntity(null, null, new Date(), null, legalEntityEntity,
                        null, null, null, null, null, null), r,
                    null, null, null, null
                )))
                .collect(toList()));
        }
    }
}
