package me.goodt.vkpht.module.notification.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import me.goodt.vkpht.module.notification.domain.dao.NotificationTemplateContentAttachmentDao;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentAttachmentEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEntity;
import me.goodt.vkpht.module.notification.api.NotificationTemplateContentAttachmentService;

@Service
@Transactional
public class NotificationTemplateContentAttachmentServiceImpl implements NotificationTemplateContentAttachmentService {

	private final NotificationTemplateContentAttachmentDao notificationTemplateContentAttachmentDao;

	public NotificationTemplateContentAttachmentServiceImpl(NotificationTemplateContentAttachmentDao notificationTemplateContentAttachmentDao) {
		this.notificationTemplateContentAttachmentDao = notificationTemplateContentAttachmentDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<NotificationTemplateContentAttachmentEntity> getAttachments(NotificationTemplateContentEntity ntc) {
		return notificationTemplateContentAttachmentDao.getAttachments(ntc);
	}

	@Override
	public List<NotificationTemplateContentAttachmentEntity> getAttachments(NotificationTemplateContentDto ntc) {
		return notificationTemplateContentAttachmentDao.getAttachmentsByNotificationTemplateContentEntityId(ntc.getId());
	}
}
