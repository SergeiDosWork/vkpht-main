package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.ReasonTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonTypeEntity;

@UtilityClass
public class ReasonTypeFactory {

    public static ReasonTypeDto create(OrgReasonTypeEntity entity) {
        return new ReasonTypeDto(entity.getId(), entity.getName());
    }
}
