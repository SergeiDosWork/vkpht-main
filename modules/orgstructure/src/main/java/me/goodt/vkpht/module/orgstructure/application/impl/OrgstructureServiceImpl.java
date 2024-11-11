package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.AssignmentService;
import me.goodt.vkpht.module.orgstructure.api.DivisionService;
import me.goodt.vkpht.module.orgstructure.api.LegalEntityTeamAssignmentService;
import me.goodt.vkpht.module.orgstructure.api.LegalService;
import me.goodt.vkpht.module.orgstructure.api.OrgstructureService;
import me.goodt.vkpht.module.orgstructure.api.PositionService;
import me.goodt.vkpht.module.orgstructure.api.RoleService;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoRequestDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.FunctionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityDto;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.FunctionDao;
import me.goodt.vkpht.module.orgstructure.domain.factory.FunctionFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionFactory;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrgstructureServiceImpl implements OrgstructureService {

	private final AssignmentService assignmentService;
	private final DivisionService divisionService;
	private final FunctionDao functionDao;
	private final RoleService roleService;
	private final LegalEntityTeamAssignmentService legalEntityTeamAssignmentService;
	private final PositionService positionService;
	private final LegalService legalService;
    private final AuthService authService;

	public List<DivisionTeamAssignmentDto> getAssignments() {
		return assignmentService.getDivisionTeamAssignmentsFull(null,
																null, null, null, false);
	}

	@Override
	public List<DivisionTeamAssignmentDto> getAssignments(Collection<Long> userIds, Collection<Long> employeeIds) {
		return assignmentService.getDivisionTeamAssignmentsFull(new ArrayList<>(userIds),
																new ArrayList<>(employeeIds), null, null, false);
	}

	@Override
	public List<LegalEntityTeamAssignmentDto> getLegalEntityAssignments(List<Long> legalEntityIds, List<Long> roleIds, List<Long> employeeIds) {
		return roleService.getLegalTeamAssignmentInfo(legalEntityIds, roleIds, employeeIds);
	}

	@Override
	public List<LegalEntityTeamAssignmentDto> getTeamLegalEntityAssignments(Long legalEntityTeamAssignmentId, Long employeeId,
																			String externalEmployeeId, Long legalEntityTeamId) {
		return legalEntityTeamAssignmentService.getLegalEntityTeamAssignments(legalEntityTeamAssignmentId, employeeId, externalEmployeeId, legalEntityTeamId);
	}

	@Override
	public Boolean checkEmployeeHeadTeamByAssignment(Long divisionTeamAssignmentId) {
		return divisionService.checkEmployeeHeadTeamByAssignment(divisionTeamAssignmentId);
	}

	@Override
	public List<Long> getDivisionTeamAssignmentIdsInLegalEntityIdsExceptAssignmentId(Long assignmentId, List<Long> legalEntityIds) {
		return divisionService.findAllInLegalEntityIdsExceptAssignmentId(assignmentId, legalEntityIds, authService.getUserEmployeeId());
	}

	@Override
	public PositionDto getPosition(Long positionId) {
		try {
			return PositionFactory.create(positionService.getPosition(positionId));
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LegalEntityDto> getLegalEntityList(List<Long> divisionIds, List<Long> divisionGroupIds) {
		return legalService.getLegalEntityList(divisionIds, divisionGroupIds);
	}

	@Override
	public List<DivisionInfoDto> getDivisionList(DivisionInfoRequestDto dto) {
		return divisionService.getDivisionInfoByParams(dto.getDivisionIds(), dto.getParentId(), dto.getLegalEntityId(), dto.getGroupIds(), dto.isWithChilds());
	}

	@Override
	public List<FunctionDto> getFunctionList() {
		return StreamSupport.stream(functionDao.findAll().spliterator(), false)
			.map(FunctionFactory::create)
			.collect(Collectors.toList());
	}
}
