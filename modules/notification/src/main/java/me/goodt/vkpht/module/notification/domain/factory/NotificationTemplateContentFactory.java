package me.goodt.vkpht.module.notification.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.notification.api.dto.NotificationRecipientDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEntity;

@UtilityClass
public class NotificationTemplateContentFactory {

    public static NotificationTemplateContentDto create(NotificationTemplateContentEntity entity,
                                                        Function<NotificationRecipientEntity, List<Long>> paramsResolver,
                                                        Function<NotificationRecipientEntity, List<String>> emailsResolver) {

        List<NotificationRecipientDto> notificationRecipientList = entity.getNotificationRecipient()
            .stream()
            .map(recipient -> NotificationRecipientFactory.create(
                    recipient,
                    paramsResolver.apply(recipient),
                    emailsResolver.apply(recipient)
                )
            )
            .collect(Collectors.toList());

        List<NotificationRecipientDto> notificationRecipientCopyList = entity.getNotificationRecipientCopy()
            .stream()
            .map(recipient -> NotificationRecipientFactory.create(
                    recipient,
                    paramsResolver.apply(recipient),
                    emailsResolver.apply(recipient)
                )
            )
            .collect(Collectors.toList());

        return new NotificationTemplateContentDto(
            entity.getId(),
            NotificationTemplateFactory.create(entity.getNotificationTemplate()),
            notificationRecipientList,
            notificationRecipientCopyList,
            NotificationReceiverSystemFactory.create(entity.getReceiverSystem()),
            entity.getIsEnabled(),
            entity.getPriority(),
            entity.getBodyJson(),
            entity.getSubstitute() != null ? NotificationTemplateContentFactory.create(entity.getSubstitute(), paramsResolver, emailsResolver) : null
        );
    }
}
