package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.TeamStatusDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.TeamStatusEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class TeamStatusAsm extends AbstractAsm<TeamStatusEntity, TeamStatusDto> {

    @Override
    public TeamStatusDto toRes(TeamStatusEntity entity) {
        TeamStatusDto dto = new TeamStatusDto();
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
    public void create(TeamStatusEntity entity, TeamStatusDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(TeamStatusEntity entity, TeamStatusDto dto) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
    }
}
