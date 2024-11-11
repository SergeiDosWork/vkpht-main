package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.SubstitutionTypeFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.SubstitutionTypeDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.SubstitutionTypeDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.SubstitutionTypeEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class SubstitutionTypeCrudService {
    private final SubstitutionTypeDao dao;
    private final UnitAccessService unitAccessService;
    private final AuthService authService;
    private final CrudDtoMapper<SubstitutionTypeEntity, SubstitutionTypeDto> mapper;

    public SubstitutionTypeDto getById(Integer id) {
        return mapper.toDto(getSecured(id));
    }

    public SubstitutionTypeDto create(SubstitutionTypeDto request) {
        SubstitutionTypeEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public SubstitutionTypeDto update(Integer id, SubstitutionTypeDto request) {
        SubstitutionTypeEntity entity = getSecured(id);
        SubstitutionTypeEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    public Page<SubstitutionTypeDto> findAll(Pageable pageable) {
        SubstitutionTypeFilter filter = SubstitutionTypeFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable).map(mapper::toDto);
    }

    public void delete(Integer id) {
        SubstitutionTypeEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    private SubstitutionTypeEntity getSecured(Integer id) {
        SubstitutionTypeEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись SubstitutionTypeEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
