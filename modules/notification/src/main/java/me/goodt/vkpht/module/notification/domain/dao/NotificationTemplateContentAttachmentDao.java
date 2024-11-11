package me.goodt.vkpht.module.notification.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentAttachmentEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationTemplateContentAttachmentEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

@Repository
public class NotificationTemplateContentAttachmentDao extends AbstractDao<NotificationTemplateContentAttachmentEntity, Long> {

	public NotificationTemplateContentAttachmentDao(EntityManager em) {
		super(NotificationTemplateContentAttachmentEntity.class, em);
	}

	public List<NotificationTemplateContentAttachmentEntity> getAttachments(NotificationTemplateContentEntity ntc) {
		return query().selectFrom(QNotificationTemplateContentAttachmentEntity.notificationTemplateContentAttachmentEntity)
			.where(QNotificationTemplateContentAttachmentEntity.notificationTemplateContentAttachmentEntity.notificationTemplateContent.eq(ntc))
			.fetch();
	}

	public List<NotificationTemplateContentAttachmentEntity> getAttachmentsByNotificationTemplateContentEntityId(Long id) {
		return query().selectFrom(QNotificationTemplateContentAttachmentEntity.notificationTemplateContentAttachmentEntity)
			.where(QNotificationTemplateContentAttachmentEntity.notificationTemplateContentAttachmentEntity.notificationTemplateContent.id.eq(id))
			.fetch();
	}

}
