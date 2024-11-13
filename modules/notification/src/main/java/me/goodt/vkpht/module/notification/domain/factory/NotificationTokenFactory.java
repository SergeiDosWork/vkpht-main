package me.goodt.vkpht.module.notification.domain.factory;

import me.goodt.vkpht.module.notification.api.dto.NotificationTokenDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTokenEntity;

public class NotificationTokenFactory {

    public static NotificationTokenDto create(NotificationTokenEntity entity) {
        return NotificationTokenDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .shortName(entity.getShortName())
            .groupName(entity.getGroupName())
            .description(entity.getDescription())
            .build();
    }
}
