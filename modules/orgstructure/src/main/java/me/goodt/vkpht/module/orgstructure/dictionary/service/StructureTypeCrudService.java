package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.StructureTypeFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.StructureTypeDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.StructureTypeDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.StructureTypeEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class StructureTypeCrudService {
    private final StructureTypeDao dao;
    private final AuthService authService;
    private final CrudDtoMapper<StructureTypeEntity, StructureTypeDto> mapper;

    public Page<StructureTypeDto> findAll(Pageable pageable) {
        StructureTypeFilter filter = StructureTypeFilter.asDefault();
        return dao.find(filter, pageable).map(mapper::toDto);
    }

    public StructureTypeDto getById(Integer id) {
        return mapper.toDto(findById(id));
    }

    public StructureTypeDto create(StructureTypeDto request) {
        StructureTypeEntity entity = mapper.toNewEntity(request);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public StructureTypeDto update(Integer id, StructureTypeDto request) {
        StructureTypeEntity entity = findById(id);
        entity = mapper.toUpdatedEntity(request, entity);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Integer id) {
        StructureTypeEntity entity = findById(id);
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    private StructureTypeEntity findById(Integer id) {
        return dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись StructureTypeEntity c идентификатором = " + id + " не найдена"));
    }
}
