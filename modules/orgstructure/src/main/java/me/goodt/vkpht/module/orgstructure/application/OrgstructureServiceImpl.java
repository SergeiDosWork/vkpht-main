package me.goodt.vkpht.module.orgstructure.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.goodt.drive.rtcore.security.AuthService;
import com.goodt.drive.rtcore.service.IViewService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.IAssignmentService;
import me.goodt.vkpht.module.orgstructure.api.IDivisionService;
import me.goodt.vkpht.module.orgstructure.api.ILegalEntityTeamAssignmentService;
import me.goodt.vkpht.module.orgstructure.api.ILegalService;
import me.goodt.vkpht.module.orgstructure.api.IPositionService;
import me.goodt.vkpht.module.orgstructure.api.IRoleService;
import me.goodt.vkpht.module.orgstructure.api.OrgstructureService;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoRequestDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.FunctionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.IEmployeeMetaLukView;
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

	private final IViewService viewService;
	private final IAssignmentService assignmentService;
	private final IDivisionService divisionService;
	private final FunctionDao functionDao;
	private final IRoleService roleService;
	private final ILegalEntityTeamAssignmentService legalEntityTeamAssignmentService;
	private final IPositionService positionService;
	private final ILegalService legalService;
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
	public List<IEmployeeMetaLukView> getEmployeeMetaLukView() {
		return viewService.getAllEmployeeMetaLukView();
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
