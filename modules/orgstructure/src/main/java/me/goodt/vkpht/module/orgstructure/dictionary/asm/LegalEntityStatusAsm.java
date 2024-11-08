package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.LegalEntityStatusDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityStatusEntity;
import me.goodt.vkpht.common.application.asm.AbstractAsm;

@Component
public class LegalEntityStatusAsm extends AbstractAsm<LegalEntityStatusEntity, LegalEntityStatusDto> {

    @Override
    public LegalEntityStatusDto toRes(LegalEntityStatusEntity entity) {
        LegalEntityStatusDto dto = new LegalEntityStatusDto();
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
    public void create(LegalEntityStatusEntity entity, LegalEntityStatusDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(LegalEntityStatusEntity entity, LegalEntityStatusDto dto) {
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        entity.setName(dto.getName());
    }
}
