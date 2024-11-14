package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.notification.domain.dao.NotificationReceiverSystemDao;
import me.goodt.vkpht.module.notification.api.dto.NotificationReceiverSystemDictDto;
import me.goodt.vkpht.module.notification.domain.mapper.NotificationReceiverSystemDictMapper;
import me.goodt.vkpht.module.notification.domain.entity.NotificationReceiverSystemEntity;
import me.goodt.vkpht.common.api.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationReceiverSystemCrudService {

    private final NotificationReceiverSystemDao dao;
    private final NotificationReceiverSystemDictMapper mapper;
    private final UnitAccessService unitAccessService;

    @Transactional(readOnly = true)
    public Page<NotificationReceiverSystemDictDto> findAll(Pageable pageable) {
        return dao.findAllIsActive(unitAccessService.getCurrentUnit(), pageable)
            .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public NotificationReceiverSystemDictDto get(Long id) {
        NotificationReceiverSystemEntity entity = findById(id);
        return mapper.toDto(entity);
    }

    public NotificationReceiverSystemDictDto create(NotificationReceiverSystemDictDto dto) {
        NotificationReceiverSystemEntity entity = mapper.toNewEntity(dto);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        return mapper.toDto(dao.save(entity));
    }

    public NotificationReceiverSystemDictDto update(Long id, NotificationReceiverSystemDictDto dto) {
        NotificationReceiverSystemEntity entity = findById(id);
        mapper.toUpdatedEntity(dto, entity);
        return mapper.toDto(dao.save(entity));
    }

    public void delete(Long id) {
        NotificationReceiverSystemEntity entity = findById(id);
        dao.delete(entity);
    }

    private NotificationReceiverSystemEntity findById(Long id) {
        NotificationReceiverSystemEntity entity = dao.findById(id)
            .orElseThrow(() -> new NotFoundException("notification_receiver_system with id=%d is not found".formatted(id)));
        return entity;
    }
}
