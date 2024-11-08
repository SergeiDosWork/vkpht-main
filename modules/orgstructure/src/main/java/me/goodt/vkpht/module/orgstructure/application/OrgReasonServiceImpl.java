package me.goodt.vkpht.module.orgstructure.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.IReasonService;
import me.goodt.vkpht.module.orgstructure.api.dto.ReasonDto;
import me.goodt.vkpht.module.orgstructure.api.dto.ReasonTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.OrgReasonDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.OrgReasonTypeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.ReasonFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonTypeEntity;

@Service
public class OrgReasonServiceImpl implements IReasonService {

    @Autowired
    private OrgReasonDao orgReasonDao;
    @Autowired
    private OrgReasonTypeDao orgReasonTypeDao;
    @Autowired
    private UnitAccessService unitAccessService;

    @Override
	public OrgReasonTypeEntity findReasonType(Long id) throws NotFoundException {
		return orgReasonTypeDao.findById(id)
			.orElseThrow(() -> new NotFoundException(String.format("ReasonTypeEntity with id %d not found", id)));
	}

    @Override
    public List<OrgReasonTypeEntity> findReasonTypeAll() {
        return StreamSupport.stream(orgReasonTypeDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public OrgReasonTypeEntity createReasonType(ReasonTypeDto dto) {
        OrgReasonTypeEntity entity = new OrgReasonTypeEntity(dto.getId(), dto.getName());
        return orgReasonTypeDao.save(entity);
    }

    @Override
    public OrgReasonTypeEntity updateReasonType(ReasonTypeDto dto) throws NotFoundException {
        OrgReasonTypeEntity entity = findReasonType(dto.getId());
        entity.setName(dto.getName());
        return orgReasonTypeDao.save(entity);
    }

	@Override
	public Integer deleteReasonType(Long id) throws NotFoundException {
		OrgReasonTypeEntity entity = findReasonType(id);
		orgReasonTypeDao.delete(entity);
		return 1;
	}

    @Override
    public OrgReasonEntity findReason(Long id) throws NotFoundException {
        OrgReasonEntity entity =  orgReasonDao.findById(id).orElseThrow(() ->
            new NotFoundException(String.format("ReasonEntity with id %d not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }

    @Override
    public List<OrgReasonEntity> findReasonAll() {
        ReasonFilter filter = ReasonFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return StreamSupport.stream(orgReasonDao.findAll(filter).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrgReasonEntity createReason(ReasonDto dto) throws NotFoundException {
        OrgReasonTypeEntity orgReasonTypeEntity = findReasonType(dto.getTypeId());
        OrgReasonEntity entity = new OrgReasonEntity(orgReasonTypeEntity, dto.getName(), dto.getDescription(), new Date(), null, unitAccessService.getCurrentUnit());
        return orgReasonDao.save(entity);
    }

    @Override
    public OrgReasonEntity updateReason(ReasonDto dto) throws NotFoundException {
        OrgReasonEntity entity = findReason(dto.getId());
        OrgReasonTypeEntity orgReasonTypeEntity = findReasonType(dto.getTypeId());
        entity.setType(orgReasonTypeEntity);
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return orgReasonDao.save(entity);
    }

    @Override
    public Long deleteReason(Long id) throws NotFoundException {
        OrgReasonEntity entity = findReason(id);
        entity.setDateTo(new Date());
        orgReasonDao.save(entity);
        return 1L;
    }
}
