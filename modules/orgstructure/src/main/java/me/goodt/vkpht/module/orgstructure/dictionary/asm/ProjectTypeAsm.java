package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTypeEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class ProjectTypeAsm extends AbstractAsm<ProjectTypeEntity, ProjectTypeDto> {

    @Override
    public ProjectTypeDto toRes(ProjectTypeEntity entity) {
        ProjectTypeDto dto = new ProjectTypeDto();
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
    public void create(ProjectTypeEntity entity, ProjectTypeDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(ProjectTypeEntity entity, ProjectTypeDto dto) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
    }
}
