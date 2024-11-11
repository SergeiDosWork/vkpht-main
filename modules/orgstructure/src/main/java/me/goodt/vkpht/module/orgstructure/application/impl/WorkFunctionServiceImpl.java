package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.WorkFunctionService;
import me.goodt.vkpht.module.orgstructure.api.dto.WorkFunctionDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.FunctionDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.WorkFunctionDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.WorkFunctionStatusDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.WorkFunctionFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.FunctionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkFunctionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkFunctionStatusEntity;

@Service
@RequiredArgsConstructor
public class WorkFunctionServiceImpl implements WorkFunctionService {

	private final FunctionDao functionDao;
	private final WorkFunctionDao workFunctionDao;
	private final WorkFunctionStatusDao workFunctionStatusDao;
	private final AuthService authService;
    private final UnitAccessService unitAccessService;

    @Override
    @Transactional(readOnly = true)
	public WorkFunctionEntity get(Long id) throws NotFoundException {
        WorkFunctionEntity dbEntity = workFunctionDao.findById(id).orElseThrow(() ->
            new NotFoundException(String.format("WorkFunctionEntity with id %d not found", id)));
        unitAccessService.checkUnitAccess(dbEntity.getUnitCode());
        return dbEntity;
	}

	@Override
    @Transactional(readOnly = true)
	public List<WorkFunctionEntity> get() {
        var filter = WorkFunctionFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();

        return workFunctionDao.find(filter, Pageable.unpaged()).getContent();
	}

	@Override
	public WorkFunctionEntity create(WorkFunctionDto dto) throws NotFoundException {
		WorkFunctionEntity entity = new WorkFunctionEntity();
        commonActions(entity, dto);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        return workFunctionDao.save(entity);
	}

	@Override
    @Transactional
	public WorkFunctionEntity update(Long id, WorkFunctionDto dto) throws NotFoundException {
		WorkFunctionEntity entity = get(id);
		commonActions(entity, dto);
		return workFunctionDao.save(entity);
	}

	@Override
    @Transactional
	public void delete(Long id) throws NotFoundException, IllegalArgumentException {
		WorkFunctionEntity entity = get(id);
		if (entity.getDateTo() != null) {
			throw new IllegalArgumentException(String.format("WorkFunction with id=%d is already deleted", id));
		}
		entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
		workFunctionDao.save(entity);
	}

	private void commonActions(WorkFunctionEntity entity, WorkFunctionDto dto) throws NotFoundException {
		Date currentDate = new Date();
		Long sessionEmployeeId = authService.getUserEmployeeId();
		if (entity.getId() == null) {
			entity.setDateFrom(currentDate);
			entity.setAuthorEmployeeId(sessionEmployeeId);
		}
		entity.setUpdateDate(currentDate);
		entity.setUpdateEmployeeId(sessionEmployeeId);
		if (dto.getPrecursorId() != null) {
			WorkFunctionEntity precursor = workFunctionDao.findById(dto.getPrecursorId())
				.orElseThrow(() -> new NotFoundException(String.format("WorkFunction with id=%d is not found", dto.getPrecursorId())));
			entity.setPrecursor(precursor);
		}
		if (dto.getFunctionId() != null) {
			FunctionEntity function = functionDao.findById(dto.getFunctionId())
				.orElseThrow(() -> new NotFoundException(String.format("Function with id=%d is not found", dto.getFunctionId())));
			entity.setFunction(function);
		}
		if (dto.getStatusId() != null) {
			WorkFunctionStatusEntity status = workFunctionStatusDao.findById(dto.getStatusId())
				.orElseThrow(() -> new NotFoundException(String.format("WorkFunctionStatus with id=%d is not found", dto.getStatusId())));
            unitAccessService.checkUnitAccess(status.getUnitCode());
			entity.setStatus(status);
		}
		entity.setFullName(dto.getFullName());
		entity.setShortName(dto.getShortName());
		entity.setAbbreviation(dto.getAbbreviation());
		entity.setIsRequiredCertificate(dto.getIsRequiredCertificate());
	}
}
