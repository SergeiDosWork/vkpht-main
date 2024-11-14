package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionPositionGradeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionPositionGradeEntity;

@UtilityClass
public class PositionPositionGradeFactory {

    public static PositionPositionGradeDto create(PositionPositionGradeEntity entity) {
        return new PositionPositionGradeDto(entity.getId().getPosition().getId(), entity.getId().getPositionGrade().getId());
    }
}
