package me.goodt.vkpht.module.notification.api;

import me.goodt.vkpht.common.api.exception.NotFoundException;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.NotificationDynamicRecipientDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationRecipientDto;

public interface NotificationRecipientService {
	List<NotificationRecipientDto> getAllNotificationRecipient();

	NotificationRecipientDto getNotificationRecipient(Long id) throws NotFoundException;

	NotificationRecipientDto createNotificationRecipient(NotificationRecipientDto dto);

	NotificationRecipientDto updateNotificationRecipient(NotificationRecipientDto dto) throws NotFoundException;

	boolean deleteNotificationRecipient(Long id);

	List<NotificationDynamicRecipientDto> getDynamicRecipients();
}
