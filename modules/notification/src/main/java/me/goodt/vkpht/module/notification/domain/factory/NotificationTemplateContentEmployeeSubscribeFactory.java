package me.goodt.vkpht.module.notification.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentEmployeeSubscribeDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEmployeeSubscribeEntity;

@UtilityClass
public class NotificationTemplateContentEmployeeSubscribeFactory {
	public static NotificationTemplateContentEmployeeSubscribeDto create(
		Long employeeId,
		List<NotificationTemplateContentEmployeeSubscribeEntity> subscribesEmployee,
		boolean isDisabledReceiverSystem,
		String receiverSystemName) {

		NotificationTemplateContentEmployeeSubscribeDto subscribeDetails = new NotificationTemplateContentEmployeeSubscribeDto();

		subscribeDetails.setEmployeeId(employeeId);
		subscribeDetails.setReceiverSystemName(receiverSystemName);

		List<NotificationTemplateContentEmployeeSubscribeDto.NotificationStateDto> subscribes = new ArrayList<>();

		subscribesEmployee.forEach(subscribe -> {
			NotificationTemplateContentEmployeeSubscribeDto.NotificationStateDto notificationStateDto =
				new NotificationTemplateContentEmployeeSubscribeDto.NotificationStateDto();

			notificationStateDto.setId(subscribe.getId());
			notificationStateDto.setIsEnabled(subscribe.getIsEnabled());
			notificationStateDto.setDescription(subscribe.getNotificationTemplateContent().getDescription());

			subscribes.add(notificationStateDto);
		});

		subscribeDetails.setSubscribes(subscribes);

		subscribeDetails.setIsEnabledAllNotifications(!isDisabledReceiverSystem);

		return subscribeDetails;
	}
}
