package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PersonPrivilegeFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.PersonPrivilegeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PrivilegeDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PersonPrivilegeDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonPrivilegeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PrivilegeEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class PersonPrivilegeCrudService {

    private final PersonPrivilegeDao dao;
    private final PrivilegeDao privilegeDao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<PersonPrivilegeEntity, PersonPrivilegeDto> mapper;

    @Transactional(readOnly = true)
    public Page<PersonPrivilegeDto> findAll(Pageable pageable) {
        var filter = PersonPrivilegeFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable)
            .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public PersonPrivilegeDto getById(Long id) {
        PersonPrivilegeEntity entity = getByIdAndUnitCode(id, unitAccessService.getCurrentUnit());
        return mapper.toDto(entity);
    }

    @Transactional
    public PersonPrivilegeDto create(PersonPrivilegeDto request) {
        PrivilegeEntity privilegeEntity = getPrivilegeByIdAndUnitCode(request.getPrivilegeId(), unitAccessService.getCurrentUnit());
        PersonPrivilegeEntity entity = mapper.toNewEntity(request);
        entity.setPrivilege(privilegeEntity);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public PersonPrivilegeDto update(Long id, PersonPrivilegeDto request) {
        PrivilegeEntity privilegeEntity = getPrivilegeByIdAndUnitCode(request.getPrivilegeId(), unitAccessService.getCurrentUnit());


        PersonPrivilegeEntity entity = getByIdAndUnitCode(id, unitAccessService.getCurrentUnit());
        entity.setPrivilege(privilegeEntity);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        PersonPrivilegeEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    @Transactional
    public void delete(Long id) {
        PersonPrivilegeEntity entity = getByIdAndUnitCode(id, unitAccessService.getCurrentUnit());
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    private PersonPrivilegeEntity getByIdAndUnitCode(Long id, String unitCode) {
        PersonPrivilegeEntity entity = dao.findByIdAndUnitCode(id, unitCode);
        if (entity == null) {
            throw new NotFoundException("Запись PersonPrivilegeEntity c идентификатором = " + id + " не найдена");
        }

        return entity;
    }

    private PrivilegeEntity getPrivilegeByIdAndUnitCode(Long id, String unitCode) {
        PrivilegeEntity entity = privilegeDao.findByIdAndUnitCode(id, unitCode);
        if (entity == null) {
            throw new NotFoundException("Запись PrivilegeEntity c идентификатором = " + id + " не найдена");
        }

        return entity;
    }
}
