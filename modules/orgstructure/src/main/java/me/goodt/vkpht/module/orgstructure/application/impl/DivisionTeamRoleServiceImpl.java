package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import me.goodt.vkpht.common.api.exception.BadRequestException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.entity.DomainObject;
import me.goodt.vkpht.module.orgstructure.api.DivisionService;
import me.goodt.vkpht.module.orgstructure.api.DivisionTeamRoleService;
import me.goodt.vkpht.module.orgstructure.api.EmployeeService;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleContainerDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleRawDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamRoleDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionImportanceDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.RoleDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamRoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.RoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamRoleFactory;

/**
 * @author Pavel Khovaylo
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class DivisionTeamRoleServiceImpl implements DivisionTeamRoleService {

    private final DivisionTeamRoleDao divisionTeamRoleDao;
    private final PositionImportanceDao positionImportanceDao;
    private final RoleDao roleDao;
    private final DivisionTeamDao divisionTeamDao;
    private final AuthService authService;
    private final DivisionService divisionService;
    private final EmployeeService employeeService;

    @Override
    public DivisionTeamRoleEntity create(DivisionTeamRoleRawDto dto) throws NotFoundException {
        DivisionTeamRoleEntity teamRole = new DivisionTeamRoleEntity();
        DivisionTeamEntity divisionTeam = divisionTeamDao.findActualById(dto.getDivisionTeamId());
        if (divisionTeam == null) {
            log.debug("Actual division team with id {} not found", dto.getDivisionTeamId());
            throw new NotFoundException(String.format("Actual division team with id %d not found", dto.getDivisionTeamId()));
        }
        RoleEntity role = roleDao.findActualById(dto.getRoleId())
            .orElseThrow(() -> {
                log.debug("Actual role with id {} not found", dto.getRoleId());
                throw new NotFoundException(String.format("Actual role with id %d not found", dto.getRoleId()));
            });
        if (dto.getPositionImportanceId() != null) {
            PositionImportanceEntity importance = positionImportanceDao.findActualById(dto.getPositionImportanceId())
                .orElseThrow(() -> {
                    log.debug("Actual importance with id {} not found", dto.getPositionImportanceId());
                    throw new NotFoundException(String.format("Actual importance with id %d not found", dto.getPositionImportanceId()));
                });
            teamRole.setPositionImportance(importance);
        }
        teamRole.setDivisionTeam(divisionTeam);
        teamRole.setRole(role);
        teamRole.setUpdateDate(new Date());
        Long sessionEmployeeId = authService.getUserEmployeeId();
        teamRole.setAuthorEmployeeId(sessionEmployeeId);
        teamRole.setUpdateEmployeeId(sessionEmployeeId);

        return divisionTeamRoleDao.save(teamRole);
    }

    @Override
    public DivisionTeamRoleEntity update(DivisionTeamRoleRawDto dto) throws NotFoundException {
        Optional<DivisionTeamRoleEntity> optionalTeamRole = divisionTeamRoleDao.findById(dto.getId());
        if (optionalTeamRole.isEmpty()) {
            log.debug("Division team role with id {} not found", dto.getId());
            throw new NotFoundException(String.format("Division team role with id %d not found", dto.getId()));
        }
        DivisionTeamRoleEntity teamRole = optionalTeamRole.get();
        if (dto.getRoleId() != null) {
            RoleEntity role = roleDao.findActualById(dto.getRoleId())
                .orElseThrow(() -> {
                    log.debug("Actual role with id {} not found", dto.getRoleId());
                    throw new NotFoundException(String.format("Actual role with id %d not found", dto.getRoleId()));
                });
            teamRole.setRole(role);
        }
        if (dto.getPositionImportanceId() != null) {
            PositionImportanceEntity importance = positionImportanceDao.findActualById(dto.getPositionImportanceId())
                .orElseThrow(() -> {
                    log.debug("Actual importance with id {} not found", dto.getPositionImportanceId());
                    throw new NotFoundException(String.format("Actual importance with id %d not found", dto.getPositionImportanceId()));
                });
            teamRole.setPositionImportance(importance);
        } else {
            teamRole.setPositionImportance(null);
        }
        teamRole.setUpdateDate(new Date());
        teamRole.setUpdateEmployeeId(authService.getUserEmployeeId());

        return divisionTeamRoleDao.save(teamRole);
    }

    @Override
    public List<DivisionTeamRoleContainerDto> findDivisionTeamRoles(Integer page, Integer size,
                                                                    Boolean successorReadinessDateFromPlusYear, Date successorReadinessDateFromStart,
                                                                    Date successorReadinessDateFromEnd, Date assignmentRotationDateFromStart,
                                                                    Date assignmentRotationDateFromEnd, Date successorDateFrom,
                                                                    List<Long> divisionTeamRoleIds, List<Long> divisionTeamIds, Long divisionId,
                                                                    String searchingValue, Long employeeSuccessorId, List<Long> legalEntityIds,
                                                                    boolean isDivisionTeamRoleIdOnlyParam) {
        boolean isFindBySearchingValue = searchingValue != null && !searchingValue.isEmpty();
        boolean withPositionSuccessors = isFindBySearchingValue && (legalEntityIds != null || divisionTeamIds != null || divisionTeamRoleIds != null || divisionId != null);
        validate(searchingValue, legalEntityIds);
        Set<Long> employeeIds = isFindBySearchingValue ? employeeService.findEmployee(null, null, null, searchingValue, legalEntityIds, true, null, Pageable.unpaged())
            .stream()
            .map(DomainObject::getId)
            .collect(Collectors.toSet()) : null;
        List<DivisionTeamRoleDto> divisionTeamRoleDtos = new ArrayList<>();
        if (!isFindBySearchingValue || !employeeIds.isEmpty()) {
            divisionTeamRoleDtos = divisionTeamRoleDao.find(DivisionTeamRoleDao.FindQueryBuilder.newInstance(page, size)
                    .employeeIds(employeeIds)
                    .divisionTeamRoleId(divisionTeamRoleIds)
                    .divisionTeamId(divisionTeamIds)
                    .successorReadinessDateFromStart(successorReadinessDateFromStart)
                    .successorReadinessDateFromEnd(successorReadinessDateFromEnd)
                    .assignmentRotationDateFromStart(assignmentRotationDateFromStart)
                    .assignmentRotationDateFromEnd(assignmentRotationDateFromEnd)
                    .successorDateFrom(successorDateFrom)
                    .successorReadinessDateFromPlusYear(successorReadinessDateFromPlusYear)
                    .divisionId(divisionId)
                    .employeeSuccessorId(employeeSuccessorId))
                .stream()
                .map(DivisionTeamRoleFactory::create)
                .collect(Collectors.toList());
        }
        return divisionService.createRoleContainers(divisionTeamRoleDtos, withPositionSuccessors || isDivisionTeamRoleIdOnlyParam);
    }

    private void validate(String searchingValue, List<Long> legalEntityId) throws BadRequestException {
        if (CollectionUtils.isNotEmpty(legalEntityId) && (StringUtils.isBlank(searchingValue))) {
            throw new BadRequestException("Request with \"legal_entity_id\" can be contain \"search\" parameter");
        }
    }
}
