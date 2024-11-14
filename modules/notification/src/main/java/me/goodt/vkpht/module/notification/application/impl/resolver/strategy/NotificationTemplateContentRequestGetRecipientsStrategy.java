package me.goodt.vkpht.module.notification.application.impl.resolver.strategy;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentRequest;

public interface NotificationTemplateContentRequestGetRecipientsStrategy {
    List<Long> getDynamicRecipientIds(NotificationTemplateContentRequest request);

    List<Long> getRecipientEmployeeIds(NotificationTemplateContentRequest request);

    List<Long> getRecipientDivisionIds(NotificationTemplateContentRequest request);

    List<String> getEmailRecipients(NotificationTemplateContentRequest request);
}
