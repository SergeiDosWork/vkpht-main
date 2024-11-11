package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.CitizenshipService;
import me.goodt.vkpht.module.orgstructure.api.dto.CitizenshipInputDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.CitizenshipDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipEntity;

@Slf4j
@Service
@Transactional
public class CitizenshipServiceImpl implements CitizenshipService {

    @Autowired
    private CitizenshipDao citizenshipDao;
	@Autowired
	private AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public CitizenshipEntity getById(Long id) throws NotFoundException {
        return citizenshipDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("CitizenshipEntity with id %d not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitizenshipEntity> getAllActual() {
        return citizenshipDao.findAllActual(Pageable.unpaged()).getContent();
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public CitizenshipEntity create(CitizenshipInputDto dto) throws NotFoundException {
        CitizenshipEntity entity = new CitizenshipEntity();
		entity.setName(dto.getName());
		entity.setShortName(dto.getShortName());
		Date currentDate = new Date();
		entity.setDateFrom(currentDate);
		entity.setUpdateDate(currentDate);
		Long sessionEmployeeId = authService.getUserEmployeeId();
		entity.setAuthorEmployeeId(sessionEmployeeId);
		entity.setUpdateEmployeeId(sessionEmployeeId);
        return citizenshipDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public CitizenshipEntity update(CitizenshipInputDto dto, Long id) throws NotFoundException {
        CitizenshipEntity entity = getById(id);
        entity.setName(dto.getName());
        entity.setShortName(dto.getShortName());
		entity.setUpdateDate(new Date());
		entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        return citizenshipDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public void delete(Long id) throws NotFoundException {
        CitizenshipEntity entity = getById(id);
		Date currentDate = new Date();
		entity.setDateTo(currentDate);
		entity.setUpdateDate(currentDate);
		entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        citizenshipDao.save(entity);
    }
}
