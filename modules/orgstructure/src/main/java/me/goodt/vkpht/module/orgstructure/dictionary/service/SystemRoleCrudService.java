package me.goodt.vkpht.module.orgstructure.dictionary.service;

import me.goodt.vkpht.module.orgstructure.domain.dao.SystemRoleDao;
import com.goodt.drive.crud.AbstractArchiveDictionaryService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.SystemRoleEntity;
import com.goodt.drive.rtcore.security.AuthService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SystemRoleCrudService extends AbstractArchiveDictionaryService<SystemRoleEntity, Integer> {

	@Getter
	@Autowired
	private SystemRoleDao archivableDao;
	@Autowired
	private AuthService authService;

	@Override
	protected void afterCreate(SystemRoleEntity entity) {
		Long sessionEmployeeId = authService.getUserEmployeeId();
		entity.setAuthorEmployeeId(sessionEmployeeId);
		entity.setUpdateEmployeeId(sessionEmployeeId);
	}

	@Override
	protected void afterUpdate(SystemRoleEntity entity) {
		entity.setUpdateEmployeeId(authService.getUserEmployeeId());
	}

	@Override
	public void delete(Integer id) {
		SystemRoleEntity entity = archivableDao.findById(id).orElseThrow(
				() -> new NotFoundException(String.format("AssignmentCategoryEntity with id = %s not found", id)));
		Date currentDate = new Date();
		entity.setDateTo(currentDate);
		entity.setUpdateDate(currentDate);
		entity.setUpdateEmployeeId(authService.getUserEmployeeId());
		archivableDao.save(entity);
	}
}
