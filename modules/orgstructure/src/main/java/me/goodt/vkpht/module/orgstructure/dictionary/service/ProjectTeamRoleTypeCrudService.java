package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import me.goodt.vkpht.common.application.impl.AbstractArchiveDictionaryService;
import me.goodt.vkpht.module.orgstructure.domain.dao.ProjectTeamRoleTypeDao;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamRoleTypeEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
public class ProjectTeamRoleTypeCrudService extends AbstractArchiveDictionaryService<ProjectTeamRoleTypeEntity, Long> {

    @Getter
    @Autowired
    private ProjectTeamRoleTypeDao archivableDao;
    @Autowired
    private AuthService authService;

    @Override
    protected void afterCreate(ProjectTeamRoleTypeEntity entity) {
        Long sessionEmployeeId = authService.getUserEmployeeId();
        entity.setAuthorEmployeeId(sessionEmployeeId);
        entity.setUpdateEmployeeId(sessionEmployeeId);
    }

    @Override
    protected void afterUpdate(ProjectTeamRoleTypeEntity entity) {
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
    }

    @Override
    public void delete(Long id) {
        ProjectTeamRoleTypeEntity entity = archivableDao.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("ProjectTeamRoleType with id = %s not found", id)));
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        archivableDao.save(entity);
    }
}
