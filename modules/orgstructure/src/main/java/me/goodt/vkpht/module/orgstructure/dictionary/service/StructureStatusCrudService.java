package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.StructureStatusFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.StructureStatusDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.StructureStatusDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.StructureStatusEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class StructureStatusCrudService {
    private final StructureStatusDao dao;
    private final AuthService authService;
    private final CrudDtoMapper<StructureStatusEntity, StructureStatusDto> mapper;

    public Page<StructureStatusDto> findAll(Pageable pageable) {
        StructureStatusFilter filter = StructureStatusFilter.asDefault();
        return dao.find(filter, pageable).map(mapper::toDto);
    }

    public StructureStatusDto getById(Integer id) {
        return mapper.toDto(getSecured(id));
    }

    public StructureStatusDto create(StructureStatusDto request) {
        StructureStatusEntity entity = mapper.toNewEntity(request);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public StructureStatusDto update(Integer id, StructureStatusDto request) {
        StructureStatusEntity entity = getSecured(id);
        entity = mapper.toUpdatedEntity(request, entity);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Integer id) {
        StructureStatusEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    private StructureStatusEntity getSecured(Integer id) {
        return dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись StructureStatusEntity c идентификатором = " + id + " не найдена"));
    }
    
}
