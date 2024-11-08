package me.goodt.vkpht.module.orgstructure.dictionary.service;

import me.goodt.vkpht.module.orgstructure.domain.dao.LocationGroupEntityDao;
import com.goodt.drive.crud.AbstractArchiveDictionaryService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.LocationGroupEntity;
import com.goodt.drive.rtcore.security.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class LocationGroupCrudService extends AbstractArchiveDictionaryService<LocationGroupEntity, Long> {

    @Getter
    private final LocationGroupEntityDao archivableDao;
    private final AuthService authService;

    @Override
    protected void afterCreate(LocationGroupEntity entity) {
        Long sessionEmployeeId = authService.getUserEmployeeId();
        entity.setAuthorEmployeeId(sessionEmployeeId);
        entity.setUpdateEmployeeId(sessionEmployeeId);
    }

    @Override
    protected void afterUpdate(LocationGroupEntity entity) {
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
    }

    @Override
    public void delete(Long id) {
        LocationGroupEntity entity = archivableDao.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("LocationGroup with id = %s not found", id)));
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        archivableDao.save(entity);
    }
}
