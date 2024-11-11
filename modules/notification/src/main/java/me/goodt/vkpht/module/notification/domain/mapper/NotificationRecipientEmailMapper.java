package me.goodt.vkpht.module.notification.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Objects;

import me.goodt.vkpht.module.notification.api.dto.NotificationRecipientEmailDto;
import com.goodt.drive.notify.application.factories.NotificationRecipientFactory;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEmailEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class NotificationRecipientEmailMapper {

    @Mapping(target = "notificationRecipient", ignore = true)
    public abstract NotificationRecipientEmailDto convertLazy(NotificationRecipientEmailEntity entity);

    public NotificationRecipientEmailDto convert(NotificationRecipientEmailEntity entity) {
        NotificationRecipientEmailDto dto = convertLazy(entity);

        if (Objects.nonNull(entity.getNotificationRecipient())) {
            dto.setNotificationRecipient(NotificationRecipientFactory.create(entity.getNotificationRecipient(), null, null));
        }

        return dto;
    }
}
