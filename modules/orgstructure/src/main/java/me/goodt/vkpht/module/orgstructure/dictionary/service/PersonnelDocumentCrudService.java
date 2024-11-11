package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PersonnelDocumentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PersonnelDocumentFormDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PersonnelDocumentTypeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PersonnelDocumentFilter;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PersonnelDocumentDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentFormEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentTypeEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class PersonnelDocumentCrudService {

    private final PersonnelDocumentDao dao;
    private final EmployeeDao employeeDao;
    private final PersonnelDocumentFormDao personnelDocumentFormDao;
    private final PersonnelDocumentTypeDao personnelDocumentTypeDao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<PersonnelDocumentEntity, PersonnelDocumentDto> mapper;

    public Page<PersonnelDocumentDto> findAll(Pageable pageable) {
        var filter = PersonnelDocumentFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable)
            .map(mapper::toDto);
    }

    public PersonnelDocumentDto getById(Long id) {
        PersonnelDocumentEntity entity = getSecured(id);
        return mapper.toDto(entity);
    }

    public PersonnelDocumentDto create(PersonnelDocumentDto createRequest) {
        PersonnelDocumentEntity entity = mapper.toNewEntity(createRequest);
        commonActions(entity, createRequest);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        dao.save(entity);
        return mapper.toDto(entity);
    }

    public PersonnelDocumentDto update(Long id, PersonnelDocumentDto updateRequest) {
        PersonnelDocumentEntity entity = getSecured(id);
        commonActions(entity, updateRequest);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        PersonnelDocumentEntity updatedEntity = mapper.toUpdatedEntity(updateRequest, entity);
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    public void delete(Long id) {
        PersonnelDocumentEntity entity = getSecured(id);
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    private PersonnelDocumentEntity getSecured(Long id) {
        PersonnelDocumentEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись PersonnelDocumentEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }

    private void commonActions(PersonnelDocumentEntity entity, PersonnelDocumentDto dto) throws NotFoundException {
        if (dto.getEmployeeId() != null) {
            EmployeeEntity employee = employeeDao.findById(dto.getEmployeeId())
                .orElseThrow(() -> new NotFoundException(String.format("Employee with id=%d is not found", dto.getEmployeeId())));
            unitAccessService.checkUnitAccess(employeeDao.findUnitByEmployee(employee.getId()));
            entity.setEmployee(employee);
        }
        if (dto.getPrecursorId() != null) {
            PersonnelDocumentEntity precursor = getSecured(dto.getPrecursorId());
            entity.setPrecursor(precursor);
        }
        if (dto.getTypeId() != null) {
            PersonnelDocumentTypeEntity type = personnelDocumentTypeDao.findById(dto.getTypeId())
                .orElseThrow(() -> new NotFoundException(String.format("PersonnelDocumentType with id=%d is not found", dto.getTypeId())));
            unitAccessService.checkUnitAccess(type.getUnitCode());
            entity.setType(type);
        }
        if (dto.getFormId() != null) {
            PersonnelDocumentFormEntity form = personnelDocumentFormDao.findById(dto.getFormId())
                .orElseThrow(() -> new NotFoundException(String.format("PersonnelDocumentForm with id=%d is not found", dto.getFormId())));
            unitAccessService.checkUnitAccess(form.getUnitCode());
            entity.setForm(form);
        }
    }
}
