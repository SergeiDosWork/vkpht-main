package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamStatusDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamStatusEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class ProjectTeamStatusAsm extends AbstractAsm<ProjectTeamStatusEntity, ProjectTeamStatusDto> {

    @Override
    public ProjectTeamStatusDto toRes(ProjectTeamStatusEntity entity) {
        ProjectTeamStatusDto dto = new ProjectTeamStatusDto();
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
    public void create(ProjectTeamStatusEntity entity, ProjectTeamStatusDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(ProjectTeamStatusEntity entity, ProjectTeamStatusDto dto) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
    }
}
