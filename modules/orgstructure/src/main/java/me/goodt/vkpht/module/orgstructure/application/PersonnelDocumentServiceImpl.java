package me.goodt.vkpht.module.orgstructure.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import com.goodt.drive.rtcore.security.AuthService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.IPersonnelDocumentService;
import me.goodt.vkpht.module.orgstructure.api.dto.PersonnelDocumentDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PersonnelDocumentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PersonnelDocumentFormDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PersonnelDocumentTypeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PersonnelDocumentFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentFormEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentTypeEntity;

@Service
@RequiredArgsConstructor
public class PersonnelDocumentServiceImpl implements IPersonnelDocumentService {

    private final PersonnelDocumentFormDao personnelDocumentFormDao;
    private final PersonnelDocumentDao personnelDocumentDao;
    private final PersonnelDocumentTypeDao personnelDocumentTypeDao;
    private final EmployeeDao employeeDao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;

    @Override
    @Transactional(readOnly = true)
    public PersonnelDocumentEntity get(Long id) {
        PersonnelDocumentEntity dbEntity = personnelDocumentDao.findById(id).orElseThrow(() ->
            new NotFoundException(String.format("PersonnelDocumentEntity with id %d not found", id)));
        unitAccessService.checkUnitAccess(dbEntity.getUnitCode());
        return dbEntity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonnelDocumentEntity> getAllActual() {
        var filter = PersonnelDocumentFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return personnelDocumentDao.find(filter, Pageable.unpaged()).getContent();
    }

    @Override
    @Transactional
    public PersonnelDocumentEntity create(PersonnelDocumentDto dto) throws NotFoundException {
        PersonnelDocumentEntity entity = new PersonnelDocumentEntity();
        commonActions(entity, dto);
        Date currentDate = new Date();
        entity.setDateFrom(currentDate);
        entity.setDateTo(null);
        entity.setUpdateDate(currentDate);
        Long sessionEmployeeId = authService.getUserEmployeeId();
        entity.setAuthorEmployeeId(sessionEmployeeId);
        entity.setUpdateEmployeeId(sessionEmployeeId);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        return personnelDocumentDao.save(entity);
    }

    @Override
    @Transactional
    public PersonnelDocumentEntity update(Long id, PersonnelDocumentDto dto) throws NotFoundException {
        PersonnelDocumentEntity entity = get(id);
        commonActions(entity, dto);
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        return personnelDocumentDao.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) throws NotFoundException {
        PersonnelDocumentEntity entity = get(id);
        if (entity.getDateTo() != null) {
            throw new RuntimeException(String.format("PersonnelDocument with id=%d is already deleted", id));
        }
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        personnelDocumentDao.save(entity);
    }

    private void commonActions(PersonnelDocumentEntity entity, PersonnelDocumentDto dto) throws NotFoundException {
        if (dto.getEmployeeId() != null) {
            EmployeeEntity employee = employeeDao.findById(dto.getEmployeeId())
                .orElseThrow(() -> new NotFoundException(String.format("Employee with id=%d is not found", dto.getEmployeeId())));
            unitAccessService.checkUnitAccess(employeeDao.findUnitByEmployee(employee.getId()));
            entity.setEmployee(employee);
        }
        if (dto.getPrecursorId() != null) {
            PersonnelDocumentEntity precursor = get(dto.getPrecursorId());
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
        entity.setName(dto.getName());
        entity.setData(dto.getData());
        entity.setExternalId(dto.getExternalId());
    }
}
