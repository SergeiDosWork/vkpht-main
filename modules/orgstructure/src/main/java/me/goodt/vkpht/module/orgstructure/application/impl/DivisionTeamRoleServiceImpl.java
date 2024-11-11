package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.extern.slf4j.Slf4j;

import me.goodt.vkpht.module.orgstructure.api.DivisionTeamRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleRawDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamRoleDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionImportanceDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.RoleDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamRoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.RoleEntity;

/**
 * @author Pavel Khovaylo
 */
@Slf4j
@Service
public class DivisionTeamRoleServiceImpl implements DivisionTeamRoleService {

    @Autowired
    private DivisionTeamRoleDao divisionTeamRoleDao;
    @Autowired
    private PositionImportanceDao positionImportanceDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private DivisionTeamDao divisionTeamDao;
    @Autowired
    private AuthService authService;

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
}
