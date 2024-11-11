package me.goodt.vkpht.module.notification.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateFilter;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateEntity;

public interface NotificationTemplateService {
	NotificationTemplateEntity getById(Long id) throws NotFoundException;

	Page<NotificationTemplateEntity> getAll(NotificationTemplateFilter filter, Pageable paging);

	NotificationTemplateEntity create(NotificationTemplateDto dto, Long employeeId) throws NotFoundException;

	NotificationTemplateEntity update(Long id, NotificationTemplateDto dto, Long employeeId) throws NotFoundException;

	void delete(Long id, Long employeeId) throws NotFoundException;

	NotificationTemplateEntity getNotificationTemplateByCode(String code) throws NotFoundException;

	List<NotificationTemplateContentEntity> getNotificationTemplateContentByNotificationTemplateId(Long notificationTemplateId);

	List<NotificationTemplateContentDto> findNotificationTemplateContentByCode(String code);

	//    List<NotificationRecipientDto> findNotificationRecipientsByTemplateContentCode(String code);

	Map<NotificationRecipientEntity, List<NotificationTemplateContentEntity>> findRecipientNameAndNotTempContentByCode(String code);

	List<NotificationTemplateContentEntity> findBySubstituteId(Long substituteId);
}
