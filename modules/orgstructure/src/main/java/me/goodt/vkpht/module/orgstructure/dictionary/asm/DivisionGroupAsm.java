package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.DivisionGroupDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionGroupEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class DivisionGroupAsm extends AbstractAsm<DivisionGroupEntity, DivisionGroupDto> {

    @Override
    public DivisionGroupDto toRes(DivisionGroupEntity entity) {
        DivisionGroupDto dto = new DivisionGroupDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setExternalId(entity.getExternalId());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }

    @Override
    public void create(DivisionGroupEntity entity, DivisionGroupDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(DivisionGroupEntity entity, DivisionGroupDto dto) {
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        entity.setName(dto.getName());
    }
}
