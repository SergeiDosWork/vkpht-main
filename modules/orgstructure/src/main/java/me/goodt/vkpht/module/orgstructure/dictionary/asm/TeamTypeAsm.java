package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.TeamTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.TeamTypeEntity;
import me.goodt.vkpht.common.application.asm.AbstractAsm;

@Component
public class TeamTypeAsm extends AbstractAsm<TeamTypeEntity, TeamTypeDto> {

    @Override
    public TeamTypeDto toRes(TeamTypeEntity entity) {
        TeamTypeDto dto = new TeamTypeDto();
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
    public void create(TeamTypeEntity entity, TeamTypeDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(TeamTypeEntity entity, TeamTypeDto dto) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
    }
}
