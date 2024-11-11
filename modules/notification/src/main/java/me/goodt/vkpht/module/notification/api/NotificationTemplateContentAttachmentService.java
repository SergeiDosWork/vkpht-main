package me.goodt.vkpht.module.notification.api;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentAttachmentEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEntity;

public interface NotificationTemplateContentAttachmentService {
	List<NotificationTemplateContentAttachmentEntity> getAttachments(NotificationTemplateContentEntity ntc);

	List<NotificationTemplateContentAttachmentEntity> getAttachments(NotificationTemplateContentDto ntc);
}
