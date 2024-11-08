package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.JobTitleClusterDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.JobTitleClusterEntity;

@Component
public class JobTitleClusterMapper implements CrudDtoMapper<JobTitleClusterEntity, JobTitleClusterDto> {
    @Override
    public JobTitleClusterEntity toNewEntity(JobTitleClusterDto dto) {
        JobTitleClusterEntity entity = new JobTitleClusterEntity();
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public JobTitleClusterEntity toUpdatedEntity(JobTitleClusterDto dto, JobTitleClusterEntity entity) {
        entity.setName(dto.getName());
        entity.setExternalId(dto.getExternalId());
        entity.setUpdateDate(new Date());
        return entity;
    }

    @Override
    public JobTitleClusterDto toDto(JobTitleClusterEntity entity) {
        JobTitleClusterDto dto = new JobTitleClusterDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setExternalId(entity.getExternalId());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }
}
