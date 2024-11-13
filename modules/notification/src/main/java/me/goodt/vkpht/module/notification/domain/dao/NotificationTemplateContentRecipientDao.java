package me.goodt.vkpht.module.notification.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentRecipientLinkEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationTemplateContentEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationTemplateContentRecipientLinkEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class NotificationTemplateContentRecipientDao extends
    AbstractDao<NotificationTemplateContentRecipientLinkEntity, Long> {

    public static final QNotificationTemplateContentRecipientLinkEntity meta =
        QNotificationTemplateContentRecipientLinkEntity.notificationTemplateContentRecipientLinkEntity;
    public static final QNotificationTemplateContentEntity notificationTemplateContent =
        QNotificationTemplateContentEntity.notificationTemplateContentEntity;

    public NotificationTemplateContentRecipientDao(EntityManager em) {
        super(NotificationTemplateContentRecipientLinkEntity.class, em);
    }

    public void deleteAllByTemplateContentId(Long templateContentId) {
        BooleanExpression deleteExpression = meta.notificationTemplateContent.id.eq(templateContentId);

        delete(deleteExpression);
    }

    public void deleteAllByRecipientId(List<Long> recipientId) {
        BooleanExpression deleteExpression = meta.notificationRecipient.id.in(recipientId);

        delete(deleteExpression);
    }

    public void deleteAllByTemplateContentIdAndNotificationRecipientId(Long templateContentId, Long notificationRecipientId) {
        BooleanExpression deleteExpression = meta.notificationTemplateContent.id.eq(templateContentId)
            .and(meta.notificationRecipient.id.eq(notificationRecipientId));
        delete(deleteExpression);
    }

    public List<NotificationTemplateContentRecipientLinkEntity> findByNotificationTemplateContentId(Long notificationTemplateContentId) {
        BooleanExpression expression = meta.notificationTemplateContent.id.eq(notificationTemplateContentId);

        return query().selectFrom(meta)
            .where(expression)
            .fetch();
    }

    public List<NotificationTemplateContentRecipientLinkEntity> findByRecipient(NotificationRecipientEntity parent) {
        return query().selectFrom(meta)
            .where(meta.notificationRecipient.eq(parent))
            .fetch();
    }

}
