package me.goodt.vkpht.module.notification.application.impl.resolver.strategy;

import java.util.ArrayList;
import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentRequest;

public class NotificationTemplateContentRequestGetRecipientsStrategyCopy implements NotificationTemplateContentRequestGetRecipientsStrategy {
	@Override
	public List<Long> getDynamicRecipientIds(NotificationTemplateContentRequest request) {
		return request.getDynamicCopyRecipientIds() != null ? request.getDynamicCopyRecipientIds() : new ArrayList<>();
	}

	@Override
	public List<Long> getRecipientEmployeeIds(NotificationTemplateContentRequest request) {
		return request.getCopyRecipientEmployeeIds() != null ? request.getCopyRecipientEmployeeIds() : new ArrayList<>();
	}

	@Override
	public List<Long> getRecipientDivisionIds(NotificationTemplateContentRequest request) {
		return request.getCopyRecipientDivisionIds() != null ? request.getCopyRecipientDivisionIds() : new ArrayList<>();
	}

    @Override
    public List<String> getEmailRecipients(NotificationTemplateContentRequest request) {
        return request.getEmailRecipientsCopy() != null ? request.getEmailRecipientsCopy() : new ArrayList<>();
    }
}
