package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.NotificationTokenService;
import me.goodt.vkpht.module.notification.api.dto.NotificationTokenDto;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTokenDao;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTokenEntity;
import me.goodt.vkpht.module.notification.domain.factory.NotificationTokenFactory;

@Service
@RequiredArgsConstructor
public class NotificationTokenServiceImpl implements NotificationTokenService {

    private final NotificationTokenDao notificationTokenDao;

    private final UnitAccessService unitAccessService;

    @Override
    public List<NotificationTokenDto> getAll() {
        return notificationTokenDao.findAllByUnitCode(unitAccessService.getCurrentUnit())
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    public NotificationTokenDto getById(Long id) throws NotFoundException {
        NotificationTokenEntity entity = findById(id);
        return NotificationTokenFactory.create(entity);
    }

    @Override
    public NotificationTokenDto create(NotificationTokenDto tokenDto) {
        NotificationTokenEntity newEntity = new NotificationTokenEntity();
        newEntity.setUnitCode(unitAccessService.getCurrentUnit());
        return NotificationTokenFactory.create(notificationTokenDao.save(mapToEntity(newEntity, tokenDto)));
    }

    @Override
    public NotificationTokenDto update(Long id, NotificationTokenDto tokenDto) throws NotFoundException {
        NotificationTokenEntity dbEntity = findById(id);
        return NotificationTokenFactory.create(notificationTokenDao.save(mapToEntity(dbEntity, tokenDto)));
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        NotificationTokenEntity dbEntity = findById(id);
        notificationTokenDao.delete(dbEntity);
    }

    private NotificationTokenEntity findById(Long id) throws NotFoundException {
        NotificationTokenEntity entity = notificationTokenDao.findById(id).orElseThrow(() ->
            new NotFoundException("NotificationTokenEntity with id = %d not found".formatted(id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }

    private NotificationTokenEntity mapToEntity(NotificationTokenEntity entity, NotificationTokenDto dto) {
        entity.setName(dto.getName());
        entity.setShortName(dto.getShortName());
        entity.setGroupName(dto.getGroupName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    private NotificationTokenDto mapToDto(NotificationTokenEntity entity) {
        NotificationTokenDto dto = new NotificationTokenDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setShortName(entity.getShortName());
        dto.setGroupName(entity.getGroupName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

}
