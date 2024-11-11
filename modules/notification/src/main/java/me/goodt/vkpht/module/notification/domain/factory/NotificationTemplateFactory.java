package me.goodt.vkpht.module.notification.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateEntity;

@UtilityClass
public final class NotificationTemplateFactory {

	public static NotificationTemplateDto create(NotificationTemplateEntity entity) {
		return new NotificationTemplateDto(
			entity.getId(),
			entity.getDateFrom(),
			entity.getDateTo(),
			entity.getAuthorEmployeeId(),
			entity.getAuthorUpdateEmployeeId(),
			entity.getDateUpdate(),
			entity.getName(),
			entity.getCode(),
			entity.getDescription(),
			entity.getIsEnabled()
		);
	}
}
