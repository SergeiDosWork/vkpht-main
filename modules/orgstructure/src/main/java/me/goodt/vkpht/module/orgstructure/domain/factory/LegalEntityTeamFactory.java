package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamEntity;

@UtilityClass
public class LegalEntityTeamFactory {

    public static LegalEntityTeamDto create(LegalEntityTeamEntity legalEntityTeamEntity) {
        return new LegalEntityTeamDto(
            legalEntityTeamEntity.getId(),
            legalEntityTeamEntity.getParent() == null ? null : legalEntityTeamEntity.getParent().getId(),
            legalEntityTeamEntity.getPrecursor() == null ? null : legalEntityTeamEntity.getPrecursor().getId(),
            legalEntityTeamEntity.getDateFrom(), legalEntityTeamEntity.getDateTo(),
            legalEntityTeamEntity.getLegalEntityEntity() == null ? null : legalEntityTeamEntity.getLegalEntityEntity().getId(),
            legalEntityTeamEntity.getType() == null ? null : legalEntityTeamEntity.getType().getId().longValue(),
            legalEntityTeamEntity.getFullName(), legalEntityTeamEntity.getShortName(), legalEntityTeamEntity.getAbbreviation(),
            legalEntityTeamEntity.getStatus() == null ? null : legalEntityTeamEntity.getStatus().getId().longValue(),
            legalEntityTeamEntity.getExternalId()
        );
    }

}
