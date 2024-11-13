package me.goodt.vkpht.module.notification.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientParameterEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationRecipientParameterEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class NotificationRecipientParametersDao extends AbstractDao<NotificationRecipientParameterEntity, Long> {

    private final static QNotificationRecipientParameterEntity meta =
        QNotificationRecipientParameterEntity.notificationRecipientParameterEntity;

    public NotificationRecipientParametersDao(EntityManager em) {
        super(NotificationRecipientParameterEntity.class, em);
    }

    public List<NotificationRecipientParameterEntity> findByParent(NotificationRecipientEntity parent) {
        return query().selectFrom(meta)
            .where(meta.parent.eq(parent))
            .fetch();
    }

    public List<NotificationRecipientParameterEntity> findByParentId(Long parentId) {
        return query().selectFrom(meta)
            .where(meta.parent.id.eq(parentId))
            .fetch();
    }

    public List<NotificationRecipientParameterEntity> findByParentIds(Collection<Long> parentIds) {
        return query().selectFrom(meta)
            .where(meta.parent.id.in(parentIds))
            .fetch();
    }

    public void deleteAllByRecipientId(Long recipientId) {
        delete(meta.parent.id.eq(recipientId));
    }

    public void deleteAllByRecipientIds(Collection<Long> recipientIds) {
        delete(meta.parent.id.in(recipientIds));
    }

}
