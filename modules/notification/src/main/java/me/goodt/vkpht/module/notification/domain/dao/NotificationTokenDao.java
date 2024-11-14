package me.goodt.vkpht.module.notification.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.module.notification.domain.entity.NotificationTokenEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationTokenEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

@Repository
public class NotificationTokenDao extends AbstractDao<NotificationTokenEntity, Long> {

    private static final QNotificationTokenEntity meta = QNotificationTokenEntity.notificationTokenEntity;

    public NotificationTokenDao(EntityManager em) {
        super(NotificationTokenEntity.class, em);
    }

    public List<NotificationTokenEntity> findAllByUnitCode(String unitCode) {
        return query().selectFrom(meta)
            .where(meta.unitCode.eq(unitCode))
            .fetch();
    }
}
