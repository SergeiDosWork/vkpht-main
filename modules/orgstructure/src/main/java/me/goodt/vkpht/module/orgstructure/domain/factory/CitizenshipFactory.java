package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.CitizenshipDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipEntity;

@UtilityClass
public class CitizenshipFactory {

    public static CitizenshipDto create(CitizenshipEntity entity) {
        return new CitizenshipDto(entity.getId(), entity.getName(), entity.getShortName(), entity.getDateFrom(), entity.getDateTo());
    }
}