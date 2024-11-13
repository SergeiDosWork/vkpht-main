package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityEntity;

@UtilityClass
public class LegalEntityFactory {

    public static LegalEntityDto create(LegalEntityEntity entity) {
        return new LegalEntityDto(entity.getId(),
            entity.getParent() == null ? null : entity.getParent().getId(),
            entity.getPrecursor() == null ? null : entity.getPrecursor().getId(),
            entity.getDateFrom(), entity.getDateTo(),
            entity.getType() == null ? null : entity.getType().getId().longValue(), entity.getIsAffiliate(),
            entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
            entity.getStatus() == null ? null : entity.getStatus().getId().longValue(),
            entity.getExternalId(),
            entity.getCostCenter() == null ? null : entity.getCostCenter().getId());
    }
}
