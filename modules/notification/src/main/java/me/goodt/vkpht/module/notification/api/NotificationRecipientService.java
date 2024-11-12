package me.goodt.vkpht.module.notification.api;

import javassist.NotFoundException;

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
