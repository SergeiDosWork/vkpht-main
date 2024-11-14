package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.WorkFunctionDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkFunctionEntity;

@UtilityClass
public class WorkFunctionFactory {

    public static WorkFunctionDto create(WorkFunctionEntity entity) {
        return new WorkFunctionDto(
            entity.getId(), entity.getPrecursor() != null ? entity.getPrecursor().getId() : null,
            entity.getDateFrom(), entity.getDateTo(), entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
            entity.getFunction() != null ? entity.getFunction().getId() : null, entity.getIsRequiredCertificate(),
            entity.getStatus() != null ? entity.getStatus().getId() : null
        );
    }
}
