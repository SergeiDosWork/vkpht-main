package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamRoleStatusDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamRoleStatusEntity;
import me.goodt.vkpht.common.application.asm.AbstractAsm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProjectTeamRoleStatusAsm extends AbstractAsm<ProjectTeamRoleStatusEntity, ProjectTeamRoleStatusDto> {

    @Override
    public ProjectTeamRoleStatusDto toRes(ProjectTeamRoleStatusEntity entity) {
        ProjectTeamRoleStatusDto dto = new ProjectTeamRoleStatusDto();
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
    public void create(ProjectTeamRoleStatusEntity entity, ProjectTeamRoleStatusDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(ProjectTeamRoleStatusEntity entity, ProjectTeamRoleStatusDto dto) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
    }
}
