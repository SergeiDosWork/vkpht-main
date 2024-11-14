package me.goodt.vkpht.module.notification.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEmailEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationLogEmailEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

@Repository
public class NotificationLogEmailDao extends AbstractDao<NotificationLogEmailEntity, Long> {

    private final QNotificationLogEmailEntity meta = QNotificationLogEmailEntity.notificationLogEmailEntity;

    public NotificationLogEmailDao(EntityManager em) {
        super(NotificationLogEmailEntity.class, em);
    }

    public List<String> findEmailByNotificationLogId(Long notificationLogId, boolean isCopy) {
        BooleanExpression expression = Expressions.allOf(
            meta.isCopy.eq(isCopy),
            meta.notificationLogId.eq(notificationLogId)
        );

        return query().select(meta.email)
            .from(meta)
            .where(expression)
            .fetch();
    }
}
