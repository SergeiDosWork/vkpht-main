package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionGroupFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionGroupDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionGroupDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.PositionGroupMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGroupEntity;

@Service
@RequiredArgsConstructor
public class PositionGroupCrudService {

    private final PositionGroupDao dao;
    private final UnitAccessService unitAccessService;
    private final PositionGroupMapper mapper;

    public Page<PositionGroupDto> findAll(Pageable pageable) {
        PositionGroupFilter filter = PositionGroupFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        Page<PositionGroupEntity> page = dao.find(filter, pageable);
        return page.map(mapper::toDto);
    }

    public PositionGroupDto get(Long id) {
        PositionGroupEntity entity = findById(id);
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return mapper.toDto(entity);
    }

    public PositionGroupDto create(PositionGroupDto request) {
        PositionGroupEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());

        return mapper.toDto(dao.save(entity));
    }

    public PositionGroupDto update(Long id, PositionGroupDto request) {
        PositionGroupEntity entity = findById(id);
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        mapper.toUpdatedEntity(request, entity);

        return mapper.toDto(dao.save(entity));
    }

    public void delete(Long id) {
        PositionGroupEntity entity = findById(id);
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        entity.setDateTo(new Date());
        dao.save(entity);
    }

    private PositionGroupEntity findById(Long id) {
        PositionGroupEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("PositionGroup with id=" + id + "is not found"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return entity;
    }
}
