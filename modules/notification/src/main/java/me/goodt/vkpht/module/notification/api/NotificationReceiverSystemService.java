package me.goodt.vkpht.module.notification.api;

import javassist.NotFoundException;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.NotificationReceiverSystemDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationReceiverSystemRequestDto;

public interface NotificationReceiverSystemService {

	List<NotificationReceiverSystemDto> getAllNotificationReceiverSystem();

	NotificationReceiverSystemDto getNotificationReceiverSystemById(Long id) throws NotFoundException;

	NotificationReceiverSystemDto createNotificationReceiverSystem(NotificationReceiverSystemRequestDto dto);

	NotificationReceiverSystemDto updateNotificationReceiverSystem(NotificationReceiverSystemRequestDto dto) throws NotFoundException;

	boolean deleteNotificationReceiverSystem(Long id);
}
