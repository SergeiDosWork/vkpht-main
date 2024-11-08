package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeDeputyDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeDeputyEntity;
import me.goodt.vkpht.common.application.asm.AbstractAsm;

@Component
public class EmployeeDeputyAsm extends AbstractAsm<EmployeeDeputyEntity, EmployeeDeputyDto> {

    @Override
    public EmployeeDeputyDto toRes(EmployeeDeputyEntity entity) {
        EmployeeDeputyDto dto = new EmployeeDeputyDto();
        dto.setId(entity.getId());
        dto.setEmployeeViceId(entity.getEmployeeViceId());
        dto.setEmployeeSubstituteId(entity.getEmployeeSubstituteId());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());

        return dto;
    }

    @Override
    public void create(EmployeeDeputyEntity entity, EmployeeDeputyDto dto) {
        entity.setEmployeeViceId(dto.getEmployeeViceId());
        entity.setEmployeeSubstituteId(dto.getEmployeeSubstituteId());
        entity.setDateFrom(dto.getDateFrom() == null ? new Date() : dto.getDateFrom());
        entity.setDateTo(dto.getDateTo());
    }

    @Override
    public void update(EmployeeDeputyEntity entity, EmployeeDeputyDto dto) {
        entity.setEmployeeViceId(dto.getEmployeeViceId());
        entity.setEmployeeSubstituteId(dto.getEmployeeSubstituteId());
        entity.setDateFrom(dto.getDateFrom() == null ? entity.getDateFrom() : dto.getDateFrom());
        entity.setDateTo(dto.getDateTo());
    }
}
