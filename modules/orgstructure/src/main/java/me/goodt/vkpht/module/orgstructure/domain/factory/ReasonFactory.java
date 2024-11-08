package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.ReasonDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonEntity;

@UtilityClass
public class ReasonFactory {

    public static ReasonDto create(OrgReasonEntity entity) {
        return new ReasonDto(entity.getId(), entity.getType().getId(), entity.getName(), entity.getDescription(), entity.getDateFrom(), entity.getDateTo());
    }
}
