package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamAssignmentStatusDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamAssignmentStatusEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProjectTeamAssignmentStatusAsm extends AbstractAsm<ProjectTeamAssignmentStatusEntity, ProjectTeamAssignmentStatusDto> {

    @Override
    public ProjectTeamAssignmentStatusDto toRes(ProjectTeamAssignmentStatusEntity entity) {
        ProjectTeamAssignmentStatusDto dto = new ProjectTeamAssignmentStatusDto();
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
    public void create(ProjectTeamAssignmentStatusEntity entity, ProjectTeamAssignmentStatusDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(ProjectTeamAssignmentStatusEntity entity, ProjectTeamAssignmentStatusDto dto) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
    }
}
