package me.goodt.vkpht.module.notification.application.impl.resolver.strategy;

import java.util.ArrayList;
import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentRequest;

public class NotificationTemplateContentRequestGetRecipientsStrategyOriginal implements NotificationTemplateContentRequestGetRecipientsStrategy {
    @Override
    public List<Long> getDynamicRecipientIds(NotificationTemplateContentRequest request) {
        return request.getDynamicRecipientIds() != null ? request.getDynamicRecipientIds() : new ArrayList<>();
    }

    @Override
    public List<Long> getRecipientEmployeeIds(NotificationTemplateContentRequest request) {
        return request.getRecipientEmployeeIds() != null ? request.getRecipientEmployeeIds() : new ArrayList<>();
    }

    @Override
    public List<Long> getRecipientDivisionIds(NotificationTemplateContentRequest request) {
        return request.getRecipientDivisionIds() != null ? request.getRecipientDivisionIds() : new ArrayList<>();
    }

    @Override
    public List<String> getEmailRecipients(NotificationTemplateContentRequest request) {
        return request.getEmailRecipients() != null ? request.getEmailRecipients() : new ArrayList<>();
    }
}
