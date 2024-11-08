package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionKrLevelDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionKrLevelEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class PositionKrLevelAsm extends AbstractAsm<PositionKrLevelEntity, PositionKrLevelDto> {

    @Override
    public PositionKrLevelDto toRes(PositionKrLevelEntity entity) {
        PositionKrLevelDto dto = new PositionKrLevelDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        return dto;
    }

    @Override
    public void create(PositionKrLevelEntity entity, PositionKrLevelDto dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }

    @Override
    public void update(PositionKrLevelEntity entity, PositionKrLevelDto dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}
