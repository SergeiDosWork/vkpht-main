package me.goodt.vkpht.module.orgstructure.dictionary.service;

import me.goodt.vkpht.module.orgstructure.domain.dao.CitizenshipDao;
import com.goodt.drive.crud.AbstractArchiveDictionaryService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipEntity;
import com.goodt.drive.rtcore.security.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CitizenshipCrudService extends AbstractArchiveDictionaryService<CitizenshipEntity, Long> {

    @Getter
    private final CitizenshipDao archivableDao;
    private final AuthService authService;

    @Override
    protected void afterCreate(CitizenshipEntity entity) {
        Long sessionEmployeeId = authService.getUserEmployeeId();
        entity.setAuthorEmployeeId(sessionEmployeeId);
        entity.setUpdateEmployeeId(sessionEmployeeId);
    }

    @Override
    protected void afterUpdate(CitizenshipEntity entity) {
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
    }

    @Override
    public void delete(Long id) {
        CitizenshipEntity entity = archivableDao.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Citizenship with id = %s not found", id)));
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        archivableDao.save(entity);
    }
}
