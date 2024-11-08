package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamRoleTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamRoleTypeEntity;
import me.goodt.vkpht.common.application.asm.AbstractAsm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProjectTeamRoleTypeAsm extends AbstractAsm<ProjectTeamRoleTypeEntity, ProjectTeamRoleTypeDto> {

    @Override
    public ProjectTeamRoleTypeDto toRes(ProjectTeamRoleTypeEntity entity) {
        ProjectTeamRoleTypeDto dto = new ProjectTeamRoleTypeDto();
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
    public void create(ProjectTeamRoleTypeEntity entity, ProjectTeamRoleTypeDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(ProjectTeamRoleTypeEntity entity, ProjectTeamRoleTypeDto dto) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
    }
}
