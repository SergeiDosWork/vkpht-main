package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.domain.dao.JobTitleClusterDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.JobTitleDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.JobTitleDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.JobTitleEntity;

@Component
@RequiredArgsConstructor
public class JobTitleMapper implements CrudDtoMapper<JobTitleEntity, JobTitleDto> {
    private final JobTitleClusterDao jobTitleClusterDao;
    private final JobTitleDao jobTitleDao;

    @Override
    public JobTitleEntity toNewEntity(JobTitleDto dto) {
        JobTitleEntity entity = new JobTitleEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public JobTitleEntity toUpdatedEntity(JobTitleDto dto, JobTitleEntity entity) {
        entity.setAbbreviation(dto.getAbbreviation());
        entity.setCode(dto.getCode());
        entity.setFullName(dto.getFullName());
        entity.setHash(dto.getHash());
        entity.setIsRequiredCertificate(Boolean.TRUE.equals(dto.getIsRequiredCertificate()) ? 1 : 0);
        entity.setShortName(dto.getShortName());
        if (dto.getClusterId() != null) {
            entity.setCluster(jobTitleClusterDao.findById(dto.getClusterId()).orElseThrow(() ->
                new NotFoundException(String.format("Cluster id = %d not found", dto.getClusterId()))));
        } else {
            entity.setCluster(null);
        }
        if (dto.getPrecursorId() != null) {
            entity.setPrecursor(jobTitleDao.findById(dto.getPrecursorId()).orElseThrow(() ->
                new NotFoundException(String.format("Precursor id = %d not found", dto.getPrecursorId()))));
        } else {
            entity.setPrecursor(null);
        }
        entity.setExternalId(dto.getExternalId());
        entity.setUpdateDate(new Date());
        return entity;
    }

    @Override
    public JobTitleDto toDto(JobTitleEntity entity) {
        JobTitleDto dto = new JobTitleDto();
        dto.setId(entity.getId());
        dto.setAbbreviation(entity.getAbbreviation());
        dto.setCode(entity.getCode());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setFullName(entity.getFullName());
        dto.setHash(entity.getHash());
        dto.setIsRequiredCertificate(entity.getIsRequiredCertificate() != null
            && entity.getIsRequiredCertificate() == 1);
        dto.setShortName(entity.getShortName());
        if (entity.getCluster() != null) {
            dto.setClusterId(entity.getCluster().getId());
        }
        if (entity.getPrecursor() != null) {
            dto.setPrecursorId(entity.getPrecursor().getId());
        }
        dto.setExternalId(entity.getExternalId());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());
        return dto;
    }
}
