package me.goodt.vkpht.module.orgstructure.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import com.goodt.drive.rtcore.data.RoleInfo;
import com.goodt.drive.rtcore.security.AuthService;
import me.goodt.vkpht.common.application.exception.BadRequestException;
import me.goodt.vkpht.common.application.exception.ForbiddenException;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.application.util.GlobalDefs;
import me.goodt.vkpht.common.application.util.TextConstants;
import me.goodt.vkpht.module.orgstructure.api.IRoleService;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.LegalEntityDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.LegalEntityTeamAssignmentConflictRoleDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.LegalEntityTeamAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.LegalEntityTeamDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.RoleDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.TeamTypeDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QDivisionTeamEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QLegalEntityTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.RoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.TeamTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.factory.LegalEntityTeamAssignmentFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.RoleFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.SystemRoleFactory;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
public class RoleServiceImpl implements IRoleService {

    private static final Integer TECH_TYPE = 2;

    @Autowired
    private LegalEntityTeamAssignmentConflictRoleDao legalEntityTeamAssignmentConflictRoleDao;
    @Autowired
    private LegalEntityTeamAssignmentDao legalEntityTeamAssignmentDao;
    @Autowired
    private LegalEntityDao legalEntityDao;
    @Autowired
    private LegalEntityTeamDao legalEntityTeamDao;
    @Autowired
    private DivisionTeamAssignmentDao divisionTeamAssignmentDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private DivisionTeamDao divisionTeamDao;
    @Autowired
    private TeamTypeDao teamTypeDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private AuthService authService;
    @Autowired
    private UnitAccessService unitAccessService;

    @Override
    public List<RoleInfo> getActualAssignableRoleList() {
        final List<RoleEntity> list = roleDao.findActualByAssignableTrue();
        return list.stream().map(r -> new RoleInfo(RoleFactory.create(r), SystemRoleFactory.create(r.getSystemRole()))).collect(toList());
    }

    @Override
    public List<LegalEntityTeamAssignmentDto> getLegalTeamAssignmentInfo(List<Long> legalEntityIds, List<Long> roleIds, List<Long> employeeIds) {
        List<LegalEntityTeamAssignmentEntity> assignments = legalEntityTeamAssignmentDao.findByLegalEntityIdsAndRoleId(legalEntityIds, roleIds, employeeIds);
        return assignments.stream()
                .filter(Objects::nonNull)
                .map(LegalEntityTeamAssignmentFactory::create)
                .collect(toList());
    }

    @Override
    @Transactional
    public LegalEntityTeamAssignmentEntity setEmployeeRole(Long legalEntityId, Long roleId, Long employeeId) throws NotFoundException {
        Optional<LegalEntityTeamAssignmentEntity> assignmentOptional = legalEntityTeamAssignmentDao.findByLegalEntityIdRoleIdEmployeeId(legalEntityId, roleId, employeeId);
        if (assignmentOptional.isPresent()) {
            log.error("Role is already exists. legalEntityId: {}, roleId: {}, employeeId: {}", legalEntityId, roleId, employeeId);
            throw new ForbiddenException(String.format("Role: %d is already exists.", roleId));
        }

        QLegalEntityTeamAssignmentEntity qleta = QLegalEntityTeamAssignmentEntity.legalEntityTeamAssignmentEntity;
        List<Long> roleIds = legalEntityTeamAssignmentDao.findAllByEmployeeIdAndLegalEntityId(employeeId, legalEntityId)
            .select(qleta.roleId).fetch();
        Set<Long> legalEntityTeamAssignmentIds = new HashSet<>(roleIds);

        QDivisionTeamEntity qdt = QDivisionTeamEntity.divisionTeamEntity;
        List<Long> divisionTeamIds = divisionTeamDao.findAllByLegalEntityId(legalEntityId).select(qdt.id).fetch();

        Set<Long> divisionTeamAssignmentIds = divisionTeamAssignmentDao.findAllByEmployeeIdAnDivisionTeamIds(employeeId, divisionTeamIds)
                .stream()
                .map(i -> i.getDivisionTeamRole().getRole().getId())
                .collect(toSet());

        boolean existConflict = legalEntityTeamAssignmentConflictRoleDao.existConflictRole(legalEntityTeamAssignmentIds, divisionTeamAssignmentIds, roleId);

        if (existConflict) {
            Integer conflictRole = legalEntityTeamAssignmentConflictRoleDao.findConflictRole(legalEntityTeamAssignmentIds, divisionTeamAssignmentIds, roleId);
            log.debug("Can't set role_id={} for employee with current role_id={}", roleId, conflictRole);
            throw new ForbiddenException(String.format("Can't set role_id=%d for employee with current role_id=%d", roleId, conflictRole));
        }

        Long sessionEmployeeId = authService.getUserEmployeeId();
        LegalEntityTeamEntity team = legalEntityTeamDao.findByLegalEntityIdAndTypeId(legalEntityId, TECH_TYPE);
        if (team == null) {
            Optional<LegalEntityEntity> legalEntity = legalEntityDao.findById(legalEntityId);
            if (legalEntity.isEmpty()) {
                log.error("No legal entity with id {} was found", legalEntityId);
                throw new NotFoundException(String.format("No legal entity with id %d was found", legalEntityId));
            }
            unitAccessService.checkUnitAccess(legalEntity.get().getUnitCode());
            Optional<TeamTypeEntity> type = teamTypeDao.findById(TECH_TYPE);
            if (type.isEmpty()) {
                log.error("No team type with id {} was found", TECH_TYPE);
                throw new NotFoundException(String.format("No team type with id %d was found", TECH_TYPE));
            }
            team = new LegalEntityTeamEntity(null, null, new Date(), null, legalEntity.get(), type.get(), "", "", "", null, null);
            team.setUpdateDate(new Date());
            team.setAuthorEmployeeId(sessionEmployeeId);
            team.setUpdateEmployeeId(sessionEmployeeId);
            team = legalEntityTeamDao.save(team);
        }
        if (team.getLegalEntityEntity() != null) {
            unitAccessService.checkUnitAccess(team.getLegalEntityEntity().getUnitCode());
        }
        Optional<EmployeeEntity> employee = employeeDao.findById(employeeId);
        if (employee.isEmpty()) {
            log.error("No employee with id {} was found", employeeId);
            throw new NotFoundException(String.format("No employee with id %d was found", employeeId));
        }

        Optional<RoleEntity> roleOptional = roleDao.findById(roleId);
        if (roleOptional.isEmpty()) {
            log.error("No role with id {} was found", roleId);
            throw new NotFoundException(String.format("No role with id %d was found", roleId));
        }

        LegalEntityTeamAssignmentEntity assignment = new LegalEntityTeamAssignmentEntity(null, new Date(), null, null, employee.get(), team, roleOptional.get(), "", "", "", null);
        assignment.setUpdateDate(new Date());
        assignment.setAuthorEmployeeId(sessionEmployeeId);
        assignment.setUpdateEmployeeId(sessionEmployeeId);
        return legalEntityTeamAssignmentDao.save(assignment);
    }

    @Override
    public LegalEntityTeamAssignmentEntity clearEmployeeRole(Long legalEntityId, Long roleId, Long employeeId) {
        Optional<LegalEntityTeamAssignmentEntity> assignmentOptional = legalEntityTeamAssignmentDao.findByLegalEntityIdRoleIdEmployeeId(legalEntityId, roleId, employeeId);
        if (assignmentOptional.isEmpty()) {
            log.error("No legal entity team assignment was found, clear employee role failed");
            return null;
        }
        LegalEntityTeamAssignmentEntity assignment = assignmentOptional.get();
        Date currentDate = new Date();
        assignment.setDateTo(currentDate);
        assignment.setUpdateDate(currentDate);
        assignment.setUpdateEmployeeId(authService.getUserEmployeeId());
        return legalEntityTeamAssignmentDao.save(assignment);
    }

    @Override
    public List<LegalEntityTeamAssignmentDto> getHrLegalEntityTeamAssignment(Long assignmentId, Long employeeId) {
        if (assignmentId == null && employeeId == null) {
            throw new BadRequestException(TextConstants.BAD_REQUEST_MESSAGE_ALL_PARAMETERS_ARE_NULL);
        }

        if (assignmentId != null && employeeId != null) {
            throw new BadRequestException(TextConstants.BAD_REQUEST_MESSAGE_ALL_PARAMETERS_ARE_NOT_NULL);
        }

        Long assignment = assignmentId != null && divisionTeamAssignmentDao.existsById(assignmentId) ? assignmentId
                : divisionTeamAssignmentDao.findIdByEmployeeId(employeeId);

        Long legalEntityId = legalEntityDao.findIdFirstByDivisionTeamAssignment(assignment, unitAccessService.getCurrentUnit());

        return legalEntityTeamAssignmentDao.findByLegalEntityIdAndSystemRoleIds(legalEntityId, GlobalDefs.HR_ROLE_SET)
                .stream()
                .map(LegalEntityTeamAssignmentFactory::create)
                .collect(toList());
    }

}
