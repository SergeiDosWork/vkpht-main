package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionCategoryDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionCategoryFilter;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionCategoryDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.PositionCategoryMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionCategoryEntity;
import com.goodt.drive.rtcore.security.AuthService;

@RequiredArgsConstructor
@Service
public class PositionCategoryCrudService {

    private final PositionCategoryDao archivableDao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;
    private final PositionCategoryMapper mapper;

    public Page<PositionCategoryDto> findAll(Pageable pageable) {
        PositionCategoryFilter filter = PositionCategoryFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        Page<PositionCategoryEntity> page = archivableDao.find(filter, pageable);
        return page.map(mapper::toDto);
    }

    public PositionCategoryDto get(Long id) {
        PositionCategoryEntity entity = archivableDao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("PositionCategoryEntity with id=%d is not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return mapper.toDto(entity);
    }

    public PositionCategoryDto create(PositionCategoryDto request) {
        PositionCategoryEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());

        return mapper.toDto(archivableDao.save(entity));
    }

    public PositionCategoryDto update(Long id, PositionCategoryDto request) {
        PositionCategoryEntity entity = archivableDao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("PositionCategoryEntity with id=%d is not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        PositionCategoryEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        updatedEntity.setUpdateEmployeeId(authService.getUserEmployeeId());

        archivableDao.save(updatedEntity);

        return mapper.toDto(updatedEntity);
    }

    public void delete(Long id) {
        PositionCategoryEntity entity = archivableDao.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("DivisionStatus with id = %s not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        archivableDao.save(entity);
    }
}
