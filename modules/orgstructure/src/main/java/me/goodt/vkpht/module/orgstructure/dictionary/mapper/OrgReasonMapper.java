package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.domain.dao.OrgReasonTypeDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ReasonDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonEntity;

@Component
@RequiredArgsConstructor
public class OrgReasonMapper implements CrudDtoMapper<OrgReasonEntity, ReasonDto> {
    private final OrgReasonTypeDao orgReasonTypeDao;

    @Override
    public OrgReasonEntity toNewEntity(ReasonDto dto) {
        OrgReasonEntity entity = new OrgReasonEntity();
        if (dto.getTypeId() != null) {
            entity.setType(orgReasonTypeDao.findById(dto.getTypeId()).orElseThrow(() ->
                new NotFoundException(String.format("Type with id = %d not found", dto.getTypeId()))));
        }
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public OrgReasonEntity toUpdatedEntity(ReasonDto dto, OrgReasonEntity entity) {
        if (dto.getTypeId() != null) {
            entity.setType(orgReasonTypeDao.findById(dto.getTypeId()).orElseThrow(() ->
                new NotFoundException(String.format("Type with id = %d not found", dto.getTypeId()))));
        }
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    @Override
    public ReasonDto toDto(OrgReasonEntity entity) {
        ReasonDto dto = new ReasonDto();
        dto.setId(entity.getId());
        if (entity.getType() != null) {
            dto.setTypeId(entity.getType().getId());
        }
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());

        return dto;
    }
}
