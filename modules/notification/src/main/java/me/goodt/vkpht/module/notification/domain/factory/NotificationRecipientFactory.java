package me.goodt.vkpht.module.notification.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.NotificationRecipientDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;

@UtilityClass
public class NotificationRecipientFactory {
	public static NotificationRecipientDto create(NotificationRecipientEntity entity, List<Long> parameters, List<String> emails) {
		return new NotificationRecipientDto(
			entity.getId(),
			entity.getName(),
			entity.getDescription(),
			parameters,
            emails
		);
	}
}
