package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.PositionProfstandardService;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionProfstandardInputDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionProfstandardDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionProfstandardEntity;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class PositionProfstandardServiceImpl implements PositionProfstandardService {

    private final PositionProfstandardDao positionProfstandardDao;

    private final PositionDao positionDao;

    @Override
    @Transactional(readOnly = true)
    public List<PositionProfstandardEntity> getAll() {
        return positionProfstandardDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PositionProfstandardEntity getById(Long id) throws NotFoundException {
        return positionProfstandardDao.findById(id).orElseThrow(() -> new NotFoundException(String.format("PositionProfstandard with id=%d is not found", id)));
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionProfstandardEntity create(PositionProfstandardInputDto dto) throws NotFoundException {
        PositionProfstandardEntity entity = new PositionProfstandardEntity();
        entity.setDateFrom(new Date());
        entity.setCode(dto.getCode());
        entity.setPosition(positionDao.findById(dto.getPositionId()).orElseThrow(() -> new NotFoundException(String.format("Position with id=%d is not found", dto.getPositionId()))));
        return positionProfstandardDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public PositionProfstandardEntity update(PositionProfstandardInputDto dto, Long id) throws NotFoundException {
        PositionProfstandardEntity entity = getById(id);
        entity.setCode(dto.getCode());
        entity.setPosition(positionDao.findById(dto.getPositionId()).orElseThrow(() -> new NotFoundException(String.format("Position with id=%d is not found", dto.getPositionId()))));
        return positionProfstandardDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public void delete(Long id) throws NotFoundException {
        PositionProfstandardEntity entity = getById(id);
        entity.setDateTo(new Date());
        positionProfstandardDao.save(entity);
    }
}
