package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.ReasonTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonTypeEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class OrgReasonTypeAsm extends AbstractAsm<OrgReasonTypeEntity, ReasonTypeDto> {

    @Override
    public ReasonTypeDto toRes(OrgReasonTypeEntity entity) {
        ReasonTypeDto dto = new ReasonTypeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    @Override
    public void create(OrgReasonTypeEntity entity, ReasonTypeDto dto) {
        entity.setName(dto.getName());
    }

    @Override
    public void update(OrgReasonTypeEntity entity, ReasonTypeDto dto) {
        entity.setName(dto.getName());
    }
}
