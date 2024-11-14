package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionGradeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGradeEntity;

@UtilityClass
public class PositionGradeFactory {

    public static PositionGradeDto create(PositionGradeEntity entity) {
        return new PositionGradeDto(entity.getId(), entity.getName(), entity.getDescription(), entity.getIndex());
    }
}
