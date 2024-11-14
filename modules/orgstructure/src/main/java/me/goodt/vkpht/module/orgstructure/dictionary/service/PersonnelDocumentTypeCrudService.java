package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.PersonnelDocumentTypeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PersonnelDocumentTypeFilter;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PersonnelDocumentTypeDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentTypeEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class PersonnelDocumentTypeCrudService {

    private final PersonnelDocumentTypeDao dao;
    private final AuthService authService;
    private final CrudDtoMapper<PersonnelDocumentTypeEntity, PersonnelDocumentTypeDto> mapper;
    private final UnitAccessService unitAccessService;

    public Page<PersonnelDocumentTypeDto> findAll(Pageable pageable) {
        var filter = PersonnelDocumentTypeFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();

        return dao.find(filter, pageable)
            .map(mapper::toDto);
    }

    public PersonnelDocumentTypeDto getById(Integer id) {
        PersonnelDocumentTypeEntity entity = getSecured(id);
        return mapper.toDto(entity);
    }

    public PersonnelDocumentTypeDto create(PersonnelDocumentTypeDto createRequest) {
        PersonnelDocumentTypeEntity entity = mapper.toNewEntity(createRequest);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        dao.save(entity);
        return mapper.toDto(entity);
    }

    public PersonnelDocumentTypeDto update(Integer id, PersonnelDocumentTypeDto updateRequest) {
        PersonnelDocumentTypeEntity entity = getSecured(id);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        PersonnelDocumentTypeEntity updatedEntity = mapper.toUpdatedEntity(updateRequest, entity);
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    public void delete(Integer id) {
        PersonnelDocumentTypeEntity entity = getSecured(id);
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    private PersonnelDocumentTypeEntity getSecured(Integer id) {
        PersonnelDocumentTypeEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись PersonnelDocumentTypeEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }

}
