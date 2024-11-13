package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeePersonDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;

@UtilityClass
public class EmployeePersonFactory {

    public static EmployeePersonDto create(EmployeeEntity entity) {
        return new EmployeePersonDto(entity.getId(),
            entity.getPerson() != null ? PersonFactory.create(entity.getPerson()) : null,
            entity.getNumber(), entity.getPhone(), entity.getEmail(), entity.getFax(),
            entity.getIsHasMobile(), entity.getIsFreelancer(), entity.getDateFrom(), entity.getDateTo(),
            entity.getExternalId());
    }
}
