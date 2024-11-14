package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.DivisionStatusDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionStatusEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class DivisionStatusAsm extends AbstractAsm<DivisionStatusEntity, DivisionStatusDto> {

    @Override
    public DivisionStatusDto toRes(DivisionStatusEntity entity) {
        DivisionStatusDto dto = new DivisionStatusDto();
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
    public void create(DivisionStatusEntity entity, DivisionStatusDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(DivisionStatusEntity entity, DivisionStatusDto dto) {
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        entity.setName(dto.getName());
    }
}
