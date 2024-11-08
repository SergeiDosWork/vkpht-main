package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionGradeFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionGradeDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionGradeDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.PositionGradeMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGradeEntity;

@Service
@RequiredArgsConstructor
public class PositionGradeCrudService {

    private final PositionGradeDao dao;
    private final UnitAccessService unitAccessService;
    private final PositionGradeMapper mapper;

    public Page<PositionGradeDto> findAll(Pageable pageRequest) {
        PositionGradeFilter filter = PositionGradeFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        Page<PositionGradeEntity> page = dao.find(filter, pageRequest);
        return page.map(mapper::toDto);
    }

    public PositionGradeDto create(PositionGradeDto request) {
        PositionGradeEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        return mapper.toDto(dao.save(entity));
    }

    public PositionGradeDto get(Long id) {
        PositionGradeEntity entity = dao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("PositionGradeEntity with id=%d is not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return mapper.toDto(entity);
    }

    public PositionGradeDto update(Long id, PositionGradeDto request) {
        PositionGradeEntity entity = dao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("PositionGradeEntity with id=%d is not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        mapper.toUpdatedEntity(request, entity);

        return mapper.toDto(dao.save(entity));
    }

    public void delete(Long id) {
        PositionGradeEntity entity = dao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("PositionGradeEntity with id=%d is not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        dao.delete(entity);
    }
}
