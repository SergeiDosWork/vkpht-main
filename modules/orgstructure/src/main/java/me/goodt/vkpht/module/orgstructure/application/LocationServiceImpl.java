package me.goodt.vkpht.module.orgstructure.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import com.goodt.drive.rtcore.service.quiz.mapper.WorkplaceMapper;
import me.goodt.vkpht.module.orgstructure.api.IWorkplaceService;
import me.goodt.vkpht.module.orgstructure.api.dto.WorkplaceDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.WorkplaceDao;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements IWorkplaceService {

    private final WorkplaceDao workplaceDao;
    private final WorkplaceMapper mapper;
    private final UnitAccessService unitAccessService;

    @Override
    public List<WorkplaceDto> getLocationByWorkplaceId(List<Long> workplaceIds) {
        String currentUnit = unitAccessService.getCurrentUnit();
        return mapper.map(workplaceDao.findActualByIds(workplaceIds, currentUnit));
    }
}
