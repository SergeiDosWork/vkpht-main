package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamTypeEntity;
import me.goodt.vkpht.common.application.asm.AbstractAsm;

@Component
public class ProjectTeamTypeAsm extends AbstractAsm<ProjectTeamTypeEntity, ProjectTeamTypeDto> {

    @Override
    public ProjectTeamTypeDto toRes(ProjectTeamTypeEntity entity) {
        ProjectTeamTypeDto dto = new ProjectTeamTypeDto();
        dto.setId(entity.getId());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setName(entity.getName());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setExternalId(entity.getExternalId());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }

    @Override
    public void create(ProjectTeamTypeEntity entity, ProjectTeamTypeDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(ProjectTeamTypeEntity entity, ProjectTeamTypeDto dto) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
    }
}
