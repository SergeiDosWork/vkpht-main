package me.goodt.vkpht.module.notification.application.impl;

import me.goodt.vkpht.common.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.notification.domain.dao.NotificationReceiverSystemDao;
import me.goodt.vkpht.module.notification.api.dto.NotificationReceiverSystemDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationReceiverSystemRequestDto;
import me.goodt.vkpht.module.notification.domain.factory.NotificationReceiverSystemFactory;
import me.goodt.vkpht.module.notification.domain.entity.NotificationReceiverSystemEntity;
import me.goodt.vkpht.module.notification.api.NotificationReceiverSystemService;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationReceiverSystemServiceImpl implements NotificationReceiverSystemService {

	private final NotificationReceiverSystemDao notificationReceiverSystemDao;
    private final UnitAccessService unitAccessService;

	@Override
	@Transactional(readOnly = true)
	public List<NotificationReceiverSystemDto> getAllNotificationReceiverSystem() {
        return notificationReceiverSystemDao.findAllIsActive(unitAccessService.getCurrentUnit(), Pageable.unpaged())
            .getContent()
            .stream()
            .map(NotificationReceiverSystemFactory::create)
            .collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public NotificationReceiverSystemDto getNotificationReceiverSystemById(Long id) throws NotFoundException {
        NotificationReceiverSystemEntity dbEntity = notificationReceiverSystemDao.findById(id)
            .orElseThrow(() -> new NotFoundException("notification_receiver_system with id=%d is not found".formatted(id)));
        unitAccessService.checkUnitAccess(dbEntity.getUnitCode());
		return NotificationReceiverSystemFactory.create(dbEntity);
	}

	@Override
    @Transactional
	public NotificationReceiverSystemDto createNotificationReceiverSystem(NotificationReceiverSystemRequestDto dto) {
		NotificationReceiverSystemEntity notificationReceiverSystemEntity = new NotificationReceiverSystemEntity(dto.getName(), dto.getDescription());
        notificationReceiverSystemEntity.setUnitCode(unitAccessService.getCurrentUnit());
        notificationReceiverSystemEntity.setIsActive(dto.getIsActive());
        notificationReceiverSystemEntity.setIsSystem(dto.getIsSystem());
        notificationReceiverSystemEntity.setIsEditableIfSystem(dto.getIsEditableIfSystem());
		return NotificationReceiverSystemFactory.create(notificationReceiverSystemDao.save(notificationReceiverSystemEntity));
	}

	@Override
	@Transactional(rollbackFor = NotFoundException.class)
	public NotificationReceiverSystemDto updateNotificationReceiverSystem(NotificationReceiverSystemRequestDto dto) throws NotFoundException {
		NotificationReceiverSystemEntity notificationReceiverSystemEntity = notificationReceiverSystemDao.findById(dto.getId())
            .orElseThrow(() -> new NotFoundException(String.format("notification_receiver_system with id=%d is not found", dto.getId())));
        unitAccessService.checkUnitAccess(notificationReceiverSystemEntity.getUnitCode());
		notificationReceiverSystemEntity.setName(dto.getName());
		notificationReceiverSystemEntity.setDescription(dto.getDescription());
        notificationReceiverSystemEntity.setIsActive(dto.getIsActive());
        notificationReceiverSystemEntity.setIsSystem(dto.getIsSystem());
        notificationReceiverSystemEntity.setIsEditableIfSystem(dto.getIsEditableIfSystem());
		return NotificationReceiverSystemFactory.create(notificationReceiverSystemDao.save(notificationReceiverSystemEntity));
	}

	@Override
    @Transactional
	public boolean deleteNotificationReceiverSystem(Long id) {
		Optional<NotificationReceiverSystemEntity> byId = notificationReceiverSystemDao.findById(id);
		if (byId.isEmpty()) {
			return false;
		}
        unitAccessService.checkUnitAccess(byId.get().getUnitCode());
		notificationReceiverSystemDao.delete(byId.get());
		return true;
	}
}
