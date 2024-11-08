package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamAssignmentTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamAssignmentTypeEntity;
import me.goodt.vkpht.common.application.asm.AbstractAsm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProjectTeamAssignmentTypeAsm extends AbstractAsm<ProjectTeamAssignmentTypeEntity, ProjectTeamAssignmentTypeDto> {

    @Override
    public ProjectTeamAssignmentTypeDto toRes(ProjectTeamAssignmentTypeEntity entity) {
        ProjectTeamAssignmentTypeDto dto = new ProjectTeamAssignmentTypeDto();
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
    public void create(ProjectTeamAssignmentTypeEntity entity, ProjectTeamAssignmentTypeDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(ProjectTeamAssignmentTypeEntity entity, ProjectTeamAssignmentTypeDto dto) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
    }
}
