package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.api.WorkplaceService;
import me.goodt.vkpht.module.orgstructure.api.dto.WorkplaceDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.WorkplaceDao;
import me.goodt.vkpht.module.orgstructure.domain.mapper.WorkplaceMapper;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements WorkplaceService {

    private final WorkplaceDao workplaceDao;
    private final WorkplaceMapper mapper;
    private final UnitAccessService unitAccessService;

    @Override
    public List<WorkplaceDto> getLocationByWorkplaceId(List<Long> workplaceIds) {
        String currentUnit = unitAccessService.getCurrentUnit();
        return mapper.map(workplaceDao.findActualByIds(workplaceIds, currentUnit));
    }
}
