package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PersonnelDocumentFormFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.PersonnelDocumentFormDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PersonnelDocumentFormDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentFormEntity;
import com.goodt.drive.rtcore.security.AuthService;

@Service
@RequiredArgsConstructor
public class PersonnelDocumentFormCrudService {

    private final PersonnelDocumentFormDao dao;
    private final AuthService authService;
    private final CrudDtoMapper<PersonnelDocumentFormEntity, PersonnelDocumentFormDto> mapper;
    private final UnitAccessService unitAccessService;

    public Page<PersonnelDocumentFormDto> findAll(Pageable pageable) {
        var filter = PersonnelDocumentFormFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();

        return dao.find(filter, pageable)
            .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public PersonnelDocumentFormDto getById(Integer id) {
        PersonnelDocumentFormEntity entity = getSecured(id);
        return mapper.toDto(entity);
    }

    @Transactional
    public PersonnelDocumentFormDto create(PersonnelDocumentFormDto createRequest) {
        PersonnelDocumentFormEntity entity = mapper.toNewEntity(createRequest);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        this.dao.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public PersonnelDocumentFormDto update(Integer id, PersonnelDocumentFormDto updateRequest) {
        PersonnelDocumentFormEntity entity = getSecured(id);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        PersonnelDocumentFormEntity updatedEntity = mapper.toUpdatedEntity(updateRequest, entity);
        this.dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    public void delete(Integer id) {
        PersonnelDocumentFormEntity entity = getSecured(id);
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    private PersonnelDocumentFormEntity getSecured(Integer id) {
        PersonnelDocumentFormEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись PersonnelDocumentFormEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
