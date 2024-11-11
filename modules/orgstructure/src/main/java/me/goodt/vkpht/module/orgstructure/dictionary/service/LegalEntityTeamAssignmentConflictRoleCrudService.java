package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.goodt.vkpht.module.orgstructure.domain.dao.LegalEntityTeamAssignmentConflictRoleDao;
import me.goodt.vkpht.common.api.exception.BadRequestException;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamAssignmentConflictRoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.RoleEntity;
import me.goodt.vkpht.common.dictionary.core.service.AbstractDictionaryService;

@Service
public class LegalEntityTeamAssignmentConflictRoleCrudService extends
		AbstractDictionaryService<LegalEntityTeamAssignmentConflictRoleEntity, Long> {

	@Getter
	@Autowired
	private LegalEntityTeamAssignmentConflictRoleDao dao;

	@Override
	protected void afterCreate(LegalEntityTeamAssignmentConflictRoleEntity entity) {
		RoleEntity legalEntityAssignedRole = entity.getLegalEntityTeamAssignmentRoleIdAssigned();
		RoleEntity divisionTeamAssignedRole = entity.getDivisionTeamAssignmentRoleIdAssigned();

		if (legalEntityAssignedRole == null && divisionTeamAssignedRole == null) {
			throw new BadRequestException("Необходимо заполнить назначаемую и/или системную роль.");
		}
	}

	@Override
	protected void afterUpdate(LegalEntityTeamAssignmentConflictRoleEntity entity) {
		RoleEntity legalEntityAssignedRole = entity.getLegalEntityTeamAssignmentRoleIdAssigned();
		RoleEntity divisionTeamAssignedRole = entity.getDivisionTeamAssignmentRoleIdAssigned();

		if (legalEntityAssignedRole == null && divisionTeamAssignedRole == null) {
			throw new BadRequestException("Необходимо заполнить назначаемую и/или системную роль.");
		}

		if (!isActualRole(legalEntityAssignedRole) && !isActualRole(divisionTeamAssignedRole)) {
			throw new BadRequestException("Назначаемая и/или системная роль должна быть актуальной.");
		}
	}

	private boolean isActualRole(RoleEntity role) {
		return role != null && role.getDateTo() == null;
	}
}
