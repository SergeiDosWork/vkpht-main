package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.CalculationMethodDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.CalculationMethodEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class CalculationMethodAsm extends AbstractAsm<CalculationMethodEntity, CalculationMethodDto> {

    @Override
    public CalculationMethodDto toRes(CalculationMethodEntity entity) {
        CalculationMethodDto dto = new CalculationMethodDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        return dto;
    }

    @Override
    public void create(CalculationMethodEntity entity, CalculationMethodDto dto) {
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }

    @Override
    public void update(CalculationMethodEntity entity, CalculationMethodDto dto) {
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}
