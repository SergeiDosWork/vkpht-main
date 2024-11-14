package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectStatusDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectStatusEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class ProjectStatusAsm extends AbstractAsm<ProjectStatusEntity, ProjectStatusDto> {

    @Override
    public ProjectStatusDto toRes(ProjectStatusEntity entity) {
        ProjectStatusDto dto = new ProjectStatusDto();
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
    public void create(ProjectStatusEntity entity, ProjectStatusDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(ProjectStatusEntity entity, ProjectStatusDto dto) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
    }
}
