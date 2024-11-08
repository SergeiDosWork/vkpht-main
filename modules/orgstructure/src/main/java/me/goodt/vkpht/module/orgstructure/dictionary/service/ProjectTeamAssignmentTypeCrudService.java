package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.crud.AbstractArchiveDictionaryService;
import me.goodt.vkpht.module.orgstructure.domain.dao.ProjectTeamAssignmentTypeDao;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamAssignmentTypeEntity;
import com.goodt.drive.rtcore.security.AuthService;

@Service
public class ProjectTeamAssignmentTypeCrudService extends
        AbstractArchiveDictionaryService<ProjectTeamAssignmentTypeEntity, Long> {

    @Getter
    @Autowired
    private ProjectTeamAssignmentTypeDao archivableDao;
    @Autowired
    private AuthService authService;

    @Override
    protected void afterCreate(ProjectTeamAssignmentTypeEntity entity) {
        Long sessionEmployeeId = authService.getUserEmployeeId();
        entity.setAuthorEmployeeId(sessionEmployeeId);
        entity.setUpdateEmployeeId(sessionEmployeeId);
    }

    @Override
    protected void afterUpdate(ProjectTeamAssignmentTypeEntity entity) {
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
    }

    @Override
    public void delete(Long id) {
        ProjectTeamAssignmentTypeEntity entity = archivableDao.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("ProjectTeamAssignmentType with id = %s not found", id)));
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        archivableDao.save(entity);
    }
}
