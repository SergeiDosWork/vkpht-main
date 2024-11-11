package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.application.impl.AbstractArchiveDictionaryService;
import me.goodt.vkpht.module.orgstructure.domain.dao.FamilyStatusDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.FamilyStatusEntity;

@Service
public class FamilyStatusCrudService extends AbstractArchiveDictionaryService<FamilyStatusEntity, Integer> {

    @Getter
    @Autowired
    private FamilyStatusDao archivableDao;
    @Autowired
    private AuthService authService;

    @Override
    protected void afterCreate(FamilyStatusEntity entity) {
        Long sessionEmployeeId = authService.getUserEmployeeId();
        entity.setAuthorEmployeeId(sessionEmployeeId);
        entity.setUpdateEmployeeId(sessionEmployeeId);
    }

    @Override
    protected void afterUpdate(FamilyStatusEntity entity) {
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
    }

    @Override
    public void delete(Integer id) {
        FamilyStatusEntity entity = archivableDao.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("FamilyStatusEntity with id = %s not found", id)));
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        archivableDao.save(entity);
    }

}
