package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.WorkplaceDto;

public interface IWorkplaceService {

    List<WorkplaceDto> getLocationByWorkplaceId(List<Long> workplaceIds);
}
