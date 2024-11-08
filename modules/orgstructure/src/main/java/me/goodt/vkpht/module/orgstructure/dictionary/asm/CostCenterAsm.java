package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.CostCenterDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.CostCenterEntity;
import me.goodt.vkpht.common.application.asm.AbstractAsm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CostCenterAsm extends AbstractAsm<CostCenterEntity, CostCenterDto> {

    @Override
    public CostCenterDto toRes(CostCenterEntity entity) {
        CostCenterDto dto = new CostCenterDto();
        dto.setId(entity.getId());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setCode(entity.getCode());
        dto.setFullName(entity.getFullName());
        dto.setShortName(entity.getShortName());
        dto.setAbbreviation(entity.getAbbreviation());
        dto.setExternalId(entity.getExternalId());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());
        dto.setUpdateDate(entity.getUpdateDate());

        return dto;
    }

    @Override
    public void create(CostCenterEntity entity, CostCenterDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(CostCenterEntity entity, CostCenterDto dto) {
        entity.setCode(dto.getCode());
        entity.setFullName(dto.getFullName());
        entity.setShortName(dto.getShortName());
        entity.setAbbreviation(dto.getAbbreviation());
        entity.setExternalId(dto.getExternalId());
        entity.setUpdateDate(new Date());
    }
}
