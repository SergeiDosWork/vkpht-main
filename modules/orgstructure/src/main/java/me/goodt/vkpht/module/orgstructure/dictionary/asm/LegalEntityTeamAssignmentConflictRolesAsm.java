package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.goodt.vkpht.module.orgstructure.domain.dao.RoleDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.LegalEntityTeamAssignmentConflictRolesDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamAssignmentConflictRoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.RoleEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class LegalEntityTeamAssignmentConflictRolesAsm extends
        AbstractAsm<LegalEntityTeamAssignmentConflictRoleEntity, LegalEntityTeamAssignmentConflictRolesDto> {

    @Autowired
    private RoleDao roleDao;

    @Override
    public LegalEntityTeamAssignmentConflictRolesDto toRes(LegalEntityTeamAssignmentConflictRoleEntity entity) {
        LegalEntityTeamAssignmentConflictRolesDto dto = new LegalEntityTeamAssignmentConflictRolesDto();

        if (entity.getLegalEntityTeamAssignmentRoleIdAssigned() != null) {
            dto.setLegalEntityTeamAssignmentRoleIdAssigned(entity.getLegalEntityTeamAssignmentRoleIdAssigned().getId());
        }
        if (entity.getLegalEntityTeamAssignmentRoleIdConflicted() != null) {
            dto.setLegalEntityTeamAssignmentRoleIdConflicted(
                    entity.getLegalEntityTeamAssignmentRoleIdConflicted().getId()
            );
        }
        dto.setId(entity.getId());
        if (entity.getDivisionTeamAssignmentRoleIdAssigned() != null)
            dto.setDivisionTeamAssignmentRoleIdAssigned(entity.getDivisionTeamAssignmentRoleIdAssigned().getId());

        return dto;
    }

    @Override
    public void create(LegalEntityTeamAssignmentConflictRoleEntity entity,
                       LegalEntityTeamAssignmentConflictRolesDto dto) {
        if (dto.getLegalEntityTeamAssignmentRoleIdConflicted() != null) {
            RoleEntity dbRole = roleDao.findActualById(dto.getLegalEntityTeamAssignmentRoleIdConflicted())
                .orElseThrow(() -> new NotFoundException(String.format("Actual LegalEntityTeamAssignmentRoleIdConflicted = %d not found",
                                                                       dto.getLegalEntityTeamAssignmentRoleIdConflicted())));

            entity.setLegalEntityTeamAssignmentRoleIdConflicted(dbRole);
        }
        if (dto.getLegalEntityTeamAssignmentRoleIdAssigned() != null) {
            RoleEntity dbRole = roleDao.findActualById(dto.getLegalEntityTeamAssignmentRoleIdAssigned())
                .orElseThrow(() -> new NotFoundException(String.format("Actual LegalEntityTeamAssignmentRoleIdAssigned = %d not found",
                                                                       dto.getLegalEntityTeamAssignmentRoleIdAssigned())));

            entity.setLegalEntityTeamAssignmentRoleIdAssigned(dbRole);
        }
        if (dto.getDivisionTeamAssignmentRoleIdAssigned() != null) {
            RoleEntity dbRole = roleDao.findActualById(dto.getDivisionTeamAssignmentRoleIdAssigned())
                .orElseThrow(() -> new NotFoundException(String.format("Actual DivisionTeamAssignmentRoleIdAssigned = %d not found",
                                                                       dto.getDivisionTeamAssignmentRoleIdAssigned())));

            entity.setDivisionTeamAssignmentRoleIdAssigned(dbRole);
        }
    }

    @Override
    public void update(LegalEntityTeamAssignmentConflictRoleEntity entity,
                       LegalEntityTeamAssignmentConflictRolesDto dto) {
        // Конфликтная роль при обновлении всегда должна быть актуальной
        if (dto.getLegalEntityTeamAssignmentRoleIdConflicted() != null) {
            RoleEntity dbRole = roleDao.findActualById(dto.getLegalEntityTeamAssignmentRoleIdConflicted())
                .orElseThrow(() -> new NotFoundException(String.format("Actual LegalEntityTeamAssignmentRoleIdConflicted = %d not found",
                                                                       dto.getLegalEntityTeamAssignmentRoleIdConflicted())));

            entity.setLegalEntityTeamAssignmentRoleIdConflicted(dbRole);
        }

        // Одна из назначаемой или системной роли может быть закрытой. Данная проверка осуществляется на уровне сервиса
        Long legalEntityAssignedRole = dto.getLegalEntityTeamAssignmentRoleIdAssigned();
        if (legalEntityAssignedRole != null) {
            entity.setLegalEntityTeamAssignmentRoleIdAssigned(roleDao.findById(legalEntityAssignedRole)
                    .orElseThrow(() -> new NotFoundException(String.format(
                            "LegalEntityTeamAssignmentRoleIdAssigned = %d not found", legalEntityAssignedRole))));
        } else {
            entity.setLegalEntityTeamAssignmentRoleIdAssigned(null);
        }
        Long divisionTeamAssignedRole = dto.getDivisionTeamAssignmentRoleIdAssigned();
        if (divisionTeamAssignedRole != null) {
            entity.setDivisionTeamAssignmentRoleIdAssigned(roleDao.findById(divisionTeamAssignedRole)
                    .orElseThrow(() -> new NotFoundException(String.format(
                            "DivisionTeamAssignmentRoleIdAssigned = %d not found", divisionTeamAssignedRole))));
        } else {
            entity.setDivisionTeamAssignmentRoleIdAssigned(null);
        }
    }
}
