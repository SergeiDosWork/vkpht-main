package me.goodt.vkpht.module.notification.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.notification.api.dto.NotificationReceiverSystemDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationReceiverSystemEntity;

@UtilityClass
public class NotificationReceiverSystemFactory {
    public static NotificationReceiverSystemDto create(NotificationReceiverSystemEntity entity) {
        return new NotificationReceiverSystemDto(entity.getId(), entity.getName(), entity.getDescription());
    }
}
