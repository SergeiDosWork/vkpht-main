package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import me.goodt.vkpht.common.application.impl.AbstractArchiveDictionaryService;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionStatusDao;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionStatusEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
public class DivisionStatusCrudService extends AbstractArchiveDictionaryService<DivisionStatusEntity, Integer> {

    @Getter
    @Autowired
    private DivisionStatusDao archivableDao;
    @Autowired
    private AuthService authService;

    @Override
    protected void afterCreate(DivisionStatusEntity entity) {
        Long sessionEmployeeId = authService.getUserEmployeeId();
        entity.setAuthorEmployeeId(sessionEmployeeId);
        entity.setUpdateEmployeeId(sessionEmployeeId);
    }

    @Override
    protected void afterUpdate(DivisionStatusEntity entity) {
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
    }

    @Override
    public void delete(Integer id) {
        DivisionStatusEntity entity = archivableDao.findById(id).orElseThrow(() ->
            new NotFoundException(String.format("DivisionStatus with id = %s not found", id)));
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        archivableDao.save(entity);
    }
}
