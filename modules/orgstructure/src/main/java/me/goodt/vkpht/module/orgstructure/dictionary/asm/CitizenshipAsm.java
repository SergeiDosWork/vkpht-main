package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.CitizenshipDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class CitizenshipAsm extends AbstractAsm<CitizenshipEntity, CitizenshipDto> {

    @Override
    public CitizenshipDto toRes(CitizenshipEntity entity) {
        CitizenshipDto dto = new CitizenshipDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setShortName(entity.getShortName());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setExternalId(entity.getExternalId());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());
        dto.setUpdateDate(entity.getUpdateDate());

        return dto;
    }

    @Override
    public void create(CitizenshipEntity entity, CitizenshipDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(CitizenshipEntity entity, CitizenshipDto dto) {
        entity.setName(dto.getName());
        entity.setShortName(dto.getShortName());
        entity.setExternalId(dto.getExternalId());
        entity.setUpdateDate(new Date());
    }
}
