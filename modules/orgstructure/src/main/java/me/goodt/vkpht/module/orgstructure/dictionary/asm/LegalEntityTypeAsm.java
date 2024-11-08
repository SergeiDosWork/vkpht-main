package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.LegalEntityTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTypeEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class LegalEntityTypeAsm extends AbstractAsm<LegalEntityTypeEntity, LegalEntityTypeDto> {

    @Override
    public LegalEntityTypeDto toRes(LegalEntityTypeEntity entity) {
        LegalEntityTypeDto dto = new LegalEntityTypeDto();
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
    public void create(LegalEntityTypeEntity entity, LegalEntityTypeDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(LegalEntityTypeEntity entity, LegalEntityTypeDto dto) {
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        entity.setName(dto.getName());
    }
}
