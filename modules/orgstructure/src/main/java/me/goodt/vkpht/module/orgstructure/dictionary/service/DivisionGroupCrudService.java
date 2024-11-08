package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.crud.AbstractArchiveDictionaryService;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionGroupDao;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionGroupEntity;
import com.goodt.drive.rtcore.security.AuthService;

@Service
public class DivisionGroupCrudService extends AbstractArchiveDictionaryService<DivisionGroupEntity, Long> {

    @Getter
    @Autowired
    private DivisionGroupDao archivableDao;
    @Autowired
    private AuthService authService;

    @Override
    protected void afterCreate(DivisionGroupEntity entity) {
        Long sessionEmployeeId = authService.getUserEmployeeId();
        entity.setAuthorEmployeeId(sessionEmployeeId);
        entity.setUpdateEmployeeId(sessionEmployeeId);
    }

    @Override
    protected void afterUpdate(DivisionGroupEntity entity) {
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
    }

    @Override
    public void delete(Long id) {
        DivisionGroupEntity entity = archivableDao.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("DivisionGroup with id = %s not found", id)));
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        archivableDao.save(entity);
    }
}
