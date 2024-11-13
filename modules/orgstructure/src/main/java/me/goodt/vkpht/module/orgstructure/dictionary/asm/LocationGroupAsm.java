package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.domain.dao.LocationGroupEntityDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.LocationGroupDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.LocationGroupEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
@RequiredArgsConstructor
public class LocationGroupAsm extends AbstractAsm<LocationGroupEntity, LocationGroupDto> {

    private final LocationGroupEntityDao locationGroupEntityDao;

    @Override
    public LocationGroupDto toRes(LocationGroupEntity entity) {
        LocationGroupDto dto = new LocationGroupDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        if (entity.getParent() != null) {
            dto.setParentId(entity.getParent().getId());
        }
        dto.setExternalId(entity.getExternalId());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());
        dto.setUpdateDate(entity.getUpdateDate());

        return dto;
    }

    @Override
    public void create(LocationGroupEntity entity, LocationGroupDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(LocationGroupEntity entity, LocationGroupDto dto) {
        entity.setName(dto.getName());
        if (dto.getParentId() != null) {
            entity.setParent(locationGroupEntityDao.findById(dto.getParentId()).orElseThrow(() ->
                new NotFoundException(String.format("Parent id = %d not found", dto.getParentId()))));
        } else {
            entity.setParent(null);
        }
        entity.setExternalId(dto.getExternalId());
        entity.setUpdateDate(new Date());
    }
}
