package me.goodt.vkpht.module.orgstructure.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.WorkplaceDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkplaceEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class WorkplaceMapper {
    public abstract List<WorkplaceDto> map(final List<WorkplaceEntity> source);

}
