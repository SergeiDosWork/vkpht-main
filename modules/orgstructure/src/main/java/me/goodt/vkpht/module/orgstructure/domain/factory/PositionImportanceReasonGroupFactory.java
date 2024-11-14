package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionImportanceReasonGroupDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceReasonGroupEntity;

@UtilityClass
public class PositionImportanceReasonGroupFactory {

    public static PositionImportanceReasonGroupDto create(PositionImportanceReasonGroupEntity entity) {
        return new PositionImportanceReasonGroupDto(entity.getId(), entity.getName(), entity.getDescription(), entity.getIsChangeable());
    }
}
