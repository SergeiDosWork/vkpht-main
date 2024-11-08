package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.FamilyStatusDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.FamilyStatusEntity;
import me.goodt.vkpht.common.application.asm.AbstractAsm;

@Component
public class FamilyStatusAsm extends AbstractAsm<FamilyStatusEntity, FamilyStatusDto> {

    @Override
    public FamilyStatusDto toRes(FamilyStatusEntity entity) {
        FamilyStatusDto dto = new FamilyStatusDto();
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
    public void create(FamilyStatusEntity entity, FamilyStatusDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(FamilyStatusEntity entity, FamilyStatusDto dto) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
    }
}
