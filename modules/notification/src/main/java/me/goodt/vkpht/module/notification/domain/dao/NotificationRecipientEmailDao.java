package me.goodt.vkpht.module.notification.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEmailEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationRecipientEmailEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

@Repository
public class NotificationRecipientEmailDao extends AbstractDao<NotificationRecipientEmailEntity, Long> {

    QNotificationRecipientEmailEntity meta = QNotificationRecipientEmailEntity.notificationRecipientEmailEntity;

    public NotificationRecipientEmailDao(EntityManager em) {
        super(NotificationRecipientEmailEntity.class, em);
    }

    public List<NotificationRecipientEmailEntity> findByRecipientIds(Set<Long> recipientIds) {
        return query().selectFrom(meta)
            .where(meta.notificationRecipientId.in(recipientIds))
            .fetch();
    }

    public List<NotificationRecipientEmailEntity> findByRecipientId(Long recipientId) {
        return query().selectFrom(meta)
            .where(meta.notificationRecipientId.eq(recipientId))
            .fetch();
    }

    public List<NotificationRecipientEmailEntity> findByRecipientIdAndEmails(Long recipientId, List<String> emails) {
        return query().selectFrom(meta)
            .where(meta.notificationRecipientId.eq(recipientId).and(meta.email.in(emails)))
            .fetch();
    }

    public List<NotificationRecipientEmailEntity> findByRecipient(NotificationRecipientEntity parent) {
        return query().selectFrom(meta)
            .where(meta.notificationRecipient.eq(parent))
            .fetch();
    }
}
